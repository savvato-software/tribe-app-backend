#!/bin/bash

# merge develop to master
# tag master
# up the version number

PWD=$(pwd)
BIN="/bin"
if [[ $PWD != *"$BIN" ]]; then
    echo "Sorry, this script assumes that it is run from the ./bin directory. [ rather than $PWD ]"
    exit 1
fi


GIT_STATUS=$(git status)
if [[ $GIT_STATUS = "On branch develop"* ]] && [[ $GIT_STATUS = *"working tree clean" ]]; then

    ENV_STAGING="staging"
    ENV_PROD="prod"

    while getopts e:t: option
    do
        case "${option}"
            in
            e) ENV=${OPTARG};;
            t) TAG_THIS_VERSION=${OPTARG};;
        esac
    done

    if [[ -z $ENV ]]; then
        echo "(1) Usage: automated-build -e [staging|prod]"
        echo "  if you are deployig to prod, you can add '-t true' to merge to master and up the version."
        exit 1
    fi

    cd ..

    if [[ $ENV != $ENV_PROD ]]; then
        echo "Error: this script is only ups the version in Production. It has no effect in other environments."
        exit 1
    fi

        if [[ $ENV = $ENV_PROD ]] && [[ $TAG_THIS_VERSION = "true" ]]; then

            VERSION=$(sed -n 's/^[[:blank:]]*<version>\([^<]\+\).*/\1/p' pom.xml | head -n 1)
            echo "Merging the develop branch into master... Tagging this version as [ "$VERSION" ]..."

            git checkout master
            git merge develop
            /usr/bin/git push
            
            git tag -a v$VERSION -m "v$VERSION, tagged automatically by the wonderful automated-build script."
            git push --tags

            git checkout develop

            NEXT_VERSION=$(./bin/increment_version.awk $VERSION)
            mvn versions:set -DnewVersion=$NEXT_VERSION

            rm pom.xml.versionsBackup
        fi

    cd -

    exit 0

else
     echo "Error: The repository is not on the 'develop' branch, or the branch is not clean."
fi
