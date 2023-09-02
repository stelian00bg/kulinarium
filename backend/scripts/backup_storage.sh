#!/bin/bash

archive=$(cat /dev/urandom | env LC_CTYPE=C tr -cd 'a-f0-9' | head -c 32)

echo Creating backup file with name: $archive.tar.bz2

docker pull loomchild/volume-backup

docker run -v backend_storage:/volume --rm --log-driver none loomchild/volume-backup backup - > $archive.tar.bz2