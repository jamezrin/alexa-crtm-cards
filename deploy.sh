#!/bin/bash

NAME="alexa-crtm-cards"
PACKAGE="target/crtmcards-1.0-SNAPSHOT.jar"

mvn clean package \
  -DskipTests

aws lambda update-function-code \
  --function-name=${NAME} \
  --zip-file=fileb://${PACKAGE}

echo "Done."
