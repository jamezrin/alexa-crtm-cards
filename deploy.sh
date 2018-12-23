#!/usr/bin/env bash

BRANCH=`git rev-parse --abbrev-ref HEAD`
[[ -z "$PROJECT_NAME" ]] && PROJECT_NAME="alexa-crtm-cards"
[[ -z "$BUILD_PATH" ]] && BUILD_PATH="target/${PROJECT_NAME}.jar"
[[ -z "$FUNCTION_NAME" ]] && echo -e "\n\033[0;31mThe function name env variable needs to be set\033[0m" && exit 1
[[ ${BRANCH} != "master" ]] && echo -e "\n\033[0;31mTried to deploy on a branch that is not master\033[0m" && exit 1

function build {
    mvn clean package \
      -DskipTests \
      -Djar.finalName=${PROJECT_NAME}
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
