#!/usr/bin/env bash

set -e

if [[ -z "$1" ]]
  then
    echo "Usage: ./tail.sh <container>"
    exit 1
fi

docker-compose logs -f "${1}"
