#!/bin/bash

# Haxwell Infrastructure Data Backup Script
#
# This script takes a mysqldump of the database, and saves it in the $HAX_APP_NAME directory.

#  That directory contains other app data, like images. It tars that whole directory,
#  It then queues the ./copy-backup-tar* script to run in two minutes.
#  If that script succeeds its copies the tar that this file creates to a remote server.
#  If that script fails to copy the tars, it reschedules itself for 2 minutes in the future, and tries again
#
#
# This script expects there to be a '$HAX_APP_NAME-backup' directory on the remote server at $BACKUP_SERVER_HOME.
#
# This script should be set to run as a cronjob. 
#
# These environment variables are expected:
# ----
#  HAX_APP_NAME # ie, eog-api
#  HAX_APP_ENVIRONMENT # ie, DEV|STAGING|PROD
#  HAX_APP_DB_NAME # ie, eog_db

if [ -z $HAX_APP_NAME ] || [ -z $HAX_APP_DB_NAME ] || [ -z $HAX_APP_ENVIRONMENT ]; then
	echo "The HAX_APP_* variables have not been set up."
	exit 1
fi

DT=$(date +%Y%m%d%H%M%S)
TODAYS_FILENAME=$HAX_APP_NAME-backup-$HAX_APP_ENVIRONMENT-$DT.tar.gz

BACKUP_SERVER_IP=98.245.226.182
BACKUP_SERVER_USER=pi
BACKUP_SERVER_HOME=/home/$BACKUP_SERVER_USER

cd /home/quizki/
mkdir /home/quizki/$HAX_APP_NAME/backup/db -p
chown quizki /home/quizki/$HAX_APP_NAME/backup/db -R
chgrp quizki /home/quizki/$HAX_APP_NAME/backup/db -R

# DUMP THE DATABASE INTO THE APP DIR
cd /home/quizki/$HAX_APP_NAME/backup/db
rm /home/quizki/$HAX_APP_NAME/backup/db/*
mysqldump $HAX_APP_DB_NAME > /home/quizki/$HAX_APP_NAME/backup/db/$HAX_APP_DB_NAME-backup-$HAX_APP_ENVIRONMENT-$DT.sql


# TAR UP THE APP DIR, IMAGES, DATABASE DUMPS, AND ALL
mkdir /home/quizki/$HAX_APP_NAME-backups -p
chown quizki /home/quizki/$HAX_APP_NAME-backups -R
chgrp quizki /home/quizki/$HAX_APP_NAME-backups -R

cd /home/quizki
tar czf /home/quizki/$HAX_APP_NAME-backups/$TODAYS_FILENAME $HAX_APP_NAME

# SEND IT ON OVER
rsync -avz /home/quizki/$HAX_APP_NAME-backups/ $BACKUP_SERVER_USER@$BACKUP_SERVER_IP:$BACKUP_SERVER_HOME/backups/$HAX_APP_NAME/

rm /home/quizki/$HAX_APP_NAME-backups -rf
