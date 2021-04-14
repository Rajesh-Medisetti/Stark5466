#!/usr/bin/env bash

source .env

set -e

IFS=', ' read -r -a DOCKER_IMAGES <<< "$APPLICATIONS"

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

build_docker_image() {
    arr=("$@")
    for deployable in "${arr[@]}"
    do
        DOCKER_IMAGE="${DOCKER_PREFIX}/${deployable}"
        docker build -t "${DOCKER_IMAGE}:${GIT_REVISION}" "${deployable}"
    done
}

tag_docker_image() {
    arr=("$@")
    for deployable in "${arr[@]}"
    do
        DOCKER_IMAGE="${DOCKER_PREFIX}/${deployable}"
        docker tag "${DOCKER_IMAGE}:${GIT_REVISION}" "${DOCKER_IMAGE}:${DOCKER_TAG}"
    done
}

push_docker_image() {
    arr=("$@")
    for deployable in "${arr[@]}"
    do
        DOCKER_IMAGE="${DOCKER_PREFIX}/${deployable}"
        docker push "${DOCKER_IMAGE}:${GIT_REVISION}"
        docker push "${DOCKER_IMAGE}:${DOCKER_TAG}"
    done
}

ENVIRONMENT=$1
COMMAND=$2

if [[ $ENVIRONMENT = "staging" ]]
then
  # shellcheck disable=SC2091
  $(aws ecr get-login --region us-east-1 --no-include-email --profile joveo-dev)
  DOCKER_TAG="latest"
  DOCKER_PREFIX="997116068644.dkr.ecr.us-east-1.amazonaws.com/${PROJECT}"
elif [[ $ENVIRONMENT = "production" ]]
then
  # shellcheck disable=SC2091
  $(aws ecr get-login --region us-east-1 --no-include-email)
  DOCKER_TAG="stable"
  DOCKER_PREFIX="485239875118.dkr.ecr.us-east-1.amazonaws.com/${PROJECT}"
elif [[ $ENVIRONMENT = "ci" ]]
then
  DOCKER_TAG="ci"
  DOCKER_PREFIX="${PROJECT}"
else
  echo "${ENVIRONMENT} not supported"
  exit 1
fi

if [[ $COMMAND = "build" ]]
then
  build_docker_image "${DOCKER_IMAGES[@]}"
elif [[ $COMMAND = "tag" ]]
then
  tag_docker_image "${DOCKER_IMAGES[@]}"
elif [[ $COMMAND = "push" ]]
then
  push_docker_image "${DOCKER_IMAGES[@]}"
elif [[ $COMMAND = "all" ]]
then
  build_docker_image "${DOCKER_IMAGES[@]}"
  tag_docker_image "${DOCKER_IMAGES[@]}"
  push_docker_image "${DOCKER_IMAGES[@]}"
else
  echo "${COMMAND} not supported"
  exit 1
fi
