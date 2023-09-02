#!/bin/bash

echo For using this script you should use archive of type *.tar.bz2, and it must have folder images, where images are stored

echo Please specify archive name ex.: something

read archive

docker pull loomchild/volume-backup

cat $archive.tar.bz2 | docker run -i -v backend_storage:/volume --rm loomchild/volume-backup restore -f -