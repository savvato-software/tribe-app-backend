#!/bin/bash

echo -e "\nThis script will download the most recent backup from the backup server, and restore the backup to this server."
echo -e "\nYou will lose all current app data.\n\n"
echo -e "If that still sounds like a good idea...\n"
read -n1 -r -p "Press space to continue..." key

while getopts a:u: option
do
    case "${option}"
        in
        a) HAX_BACKUP_SERVER_IP=${OPTARG,,};; # The ,, at the end makes the variable lowercase. w00t
        u) HAX_BACKUP_SERVER_USER_NAME=${OPTARG,,};;
    esac
done

if [ -z $HAX_APP_NAME ] || [ -z $HAX_APP_ENVIRONMENT ]; then
    echo "Either the environment variable HAX_APP_NAME or HAX_APP_ENVIRONMENT is not set :("
    exit 1
fi

if [ -z $HAX_BACKUP_SERVER_USER_NAME ] || [ -z $HAX_BACKUP_SERVER_IP ]; then
    echo "You need to define the HAX_BACKUP_SERVER_USER_NAME and the HAX_BACKUP_SERVER_IP."
    echo
    echo "Usage 2:"
    echo "./refresh-data-w-latest-backup.sh -a [backup-server-ip] -u [backup-server-user-name]"
    echo
    exit 1
fi

if [ "${HAX_APP_ENVIRONMENT,,}" = "prod" ]; then 
	echo "********"
	echo "HEY! HEY HEY! You are in a PRODUCTION environment! Are you super duper sure you want to restore the most recent backup?"
	echo -e "\n\nYou will lose ALL current app data.\n\n"
	echo -e "If that STILL sounds like a good idea...\n"
	read -n1 -r -p "Press space to continue..." key	
fi

echo -e "\n\nOK, heeerre we go..."

# remote ls the backup server and get the name of the most recent backup
FILENAME=$(ssh $HAX_BACKUP_SERVER_USER_NAME@$HAX_BACKUP_SERVER_IP ls -t /home/$HAX_BACKUP_SERVER_USER_NAME/backups/$HAX_APP_NAME | grep -i $HAX_APP_ENVIRONMENT | head -n 1)

# scp that file local-side
mkdir -p /home/quizki/work
cd /home/quizki/work
echo "Downloading the latest backup from $HAX_BACKUP_SERVER_IP"
scp -q -o LogLevel=QUIET $HAX_BACKUP_SERVER_USER_NAME@$HAX_BACKUP_SERVER_IP:/home/$HAX_BACKUP_SERVER_USER_NAME/backups/$HAX_APP_NAME/$FILENAME /home/quizki/work/$FILENAME

# untar it into the data directory for the app
tar xf $FILENAME 

echo "Completely re-creating the database...."
mysql -e "DROP DATABASE $HAX_APP_DB_NAME;"
mysql -e "CREATE DATABASE $HAX_APP_DB_NAME;"
mysql $HAX_APP_DB_NAME < `find . -name *.sql | head -n 1` # there should only be one, but regardless, find the first SQL file in the backup, and import it to our DB

echo "Putting the images in place..."
mkdir /home/quizki/$HAX_APP_NAME/images -p
cp $HAX_APP_NAME/images /home/quizki/$HAX_APP_NAME/images -R

echo "Cleaning up..."
rm /home/quizki/work/$HAX_APP_NAME -rf
rm /home/quizki/work/$FILENAME

echo -e "Done.\n"


