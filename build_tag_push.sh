#!/usr/bin/env bash

source .env

set -e

if [[ -z "$1" ]]
  then
    echo "Usage: ./build_tag_push.sh <environment> <command>"
    exit 1
fi

if [[ -z "$2" ]]
  then
    echo "Usage: ./build_tag_push.sh <environment> <command>"
    exit 1
fi

ENVIRONMENT=$1
COMMAND=$2

if [[ $ENVIRONMENT = "staging" ]]
then
  echo "Setting up for SNAPSHOT deployment"
  # shellcheck disable=SC2091
elif [[ $ENVIRONMENT = "production" ]]
then
  # shellcheck disable=SC2091
  ./eqr-test-sdk/increment_tag.sh
else
  echo "${ENVIRONMENT} not supported"
  exit 1
fi

if [[ $COMMAND = "all" ]]
then
  docker build -f eqr-test-sdk/Dockerfile .
else
  echo "${COMMAND} not supported"
  exit 1
fi
