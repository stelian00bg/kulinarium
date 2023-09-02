#!/bin/bash

echo Enter 1 for restoring from backup and install, or 2 for clean installation without restore

read choice

if [ $choice -eq 1 ]
then
	echo Restoring and installing
	/bin/bash restore_storage.sh
	cd ..
	docker-compose build
	echo Installation and backup complete
else
	cd ..
	docker-compose build
	echo Installation complete
fi