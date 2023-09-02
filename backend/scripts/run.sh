#!/bin/bash

echo Enter 1 for only running the application, or 2 for installing it and then running it

read choice

if [ $choice -eq 1 ]
then
	echo Running the application
	cd ..
	docker-compose up
else
	/bin/bash install.sh
    docker-compose up
fi