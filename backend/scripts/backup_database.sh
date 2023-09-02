#!/bin/bash

docker exec -t postgres-kulinarium pg_dumpall -c -U postgres > dump_"$(date +%d-%m-%Y"_"%H_%M_%S)".sql