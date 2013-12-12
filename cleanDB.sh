#!/bin/bash

if [ -z ${PSQL+x} ]; then
    export PSQL="/Applications/Postgres93.app/Contents/MacOS/bin/psql -c";
fi

echo $PSQL
echo `$PSQL 'drop database fenix'`
echo `$PSQL 'create database fenix'`

