#!/bin/sh

# HAXWELL INFRASTRUCTURE - Update and Start The Backend
# ----

# Be sure that the branch of the git repo that this server is building off of is the correct one..
#  Master for PROD, Develop for STAGING, and the local dev branch for DEV

# 
# This script is added to crontab, and then run when the machine starts.
#  It keeps me from having to initialize the machine, this script now does
#  it for me.
#
#  crontab: @reboot <this-file-name.sh>       # create cron entry which runs this file at reboot

# To see if this file has been added to cron already:
#  crontab -l

# Also, there is a file, ~/.my.cnf, which has mysql login info

eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_rsa

cd /home/quizki/src/$HAX_APP_NAME

pwd

git pull

export JAVA_HOME=/home/quizki/apps/java/current

# backup the out file
[ -f out ] && mv out out-backup-$(date +%Y%m%d%H%M%S).txt 

/home/quizki/apps/maven/current/bin/mvn spring-boot:run > /home/quizki/out &

exit 0;

