#!/usr/bin/env bash

set -e

if [[ -z "$1" ]]
  then
    echo "Usage: ./restart.sh <container>"
    exit 1
fi

docker-compose restart "${1}"
