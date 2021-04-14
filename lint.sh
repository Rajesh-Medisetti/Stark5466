#!/usr/bin/env bash

source .env

set -e

if [[ -z "$1" ]]
  then
    echo "Usage: ./lint.sh <environment>"
    exit 1
fi

ENVIRONMENT=$1

docker build -t "${PROJECT}"/lint -f Dockerfile .

if [[ $ENVIRONMENT = "development" ]]
then
  docker run -v "${PWD}:/usr/src" -v /usr/src/data -v /usr/src/react-application/node_modules -v /usr/src/java-application/target -v "${PWD}/data/pre-commit-cache:/root/.cache" "${PROJECT}"/lint:latest
elif [[ $ENVIRONMENT = "ci" ]]
then
  docker run "${PROJECT}"/lint:latest
else
  echo "${ENVIRONMENT} not supported"
  exit 1
fi
