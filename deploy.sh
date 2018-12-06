#!/bin/bash

NAME="alexa-crtm-cards"
BUILD_PATH="target/${NAME}.jar"

[[ -z "$FUNCTION_NAME" ]] && echo "The function name env variable needs to be set" && exit 1

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

if build && deploy; then
    echo -e "\n\033[0;32mSuccessfully built and deployed\033[0m" && exit 0
else
    echo -e "\n\033[0;31mAn error occurred while executing this script\033[0m" && exit 1
fi
