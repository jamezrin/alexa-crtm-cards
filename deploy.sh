#!/bin/bash

#NAME="alexa-crtm-cards-server"
NAME="alexa-crtm-cards"
BUILD_PATH="target/${NAME}.jar"
FUNCTION_NAME="alexa-crtm-cards-test-server"

function build {
    mvn clean package \
      -DskipTests \
      -Djar.finalName=${NAME}
}

function deploy {
    aws lambda update-function-code \
     --function-name=${FUNCTION_NAME} \
     --zip-file=fileb://${BUILD_PATH}
}

build && deploy && echo "Successfully built and deployed" && exit 0
echo "An error occurred while executing this script" && exit 1
