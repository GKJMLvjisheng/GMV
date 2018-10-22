#!/bin/bash
echo 'start autoBuild...'

cd oas-server && gradle clean bootjar

scp oas/build/libs/oas-server-0.0.1-SNAPSHOT.jar ubuntu@aws:

echo 'connect AWS Server...'

ssh ubuntu@aws

echo 'Stop AWS Server...'

sudo systemctl stop oas-server

mv ~/oas-server-0.0.1-SNAPSHOT.jar /home/ubuntu/tools/oas-server/

echo 'Restart AWS Server...'

sudo systemctl start oas-server

echo 'end autoBuild!'

