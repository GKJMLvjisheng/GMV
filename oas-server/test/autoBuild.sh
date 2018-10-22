#!/bin/bash
echo 'start autoBuild...'

scp oas/build/libs/oas-server-0.0.1-SNAPSHOT.jar ubuntu@aws:

ssh ubuntu@aws

sudo systemctl stop oas-server

mv ~/oas-server-0.0.1-SNAPSHOT.jar /home/ubuntu/tools/oas-server/

sudo systemctl start oas-server

echo 'end autoTest!'