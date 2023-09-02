#!/bin/bash

read -p "Enter the name of backup sql file from which you want to restore database: " dump

cat $dump.sql | docker exec -i postgres-kulinarium psql -U postgres