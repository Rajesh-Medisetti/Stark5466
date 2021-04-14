#!/usr/bin/env bash

source .env

set -e

if [[ -z "$1" ]]
  then
    echo "Usage: ./test_runner.sh <environment>"
    exit 1
fi

if [[ "$1" != "development" ]] && [[ "$1" != "ci" ]]
   then
    echo "environment=${1} is not supported."
    exit 1
fi

ENVIRONMENT=$1

IFS=', ' read -r -a arr <<< "$APPLICATIONS"

for i in "${arr[@]}"
do
   cd "$i"
   ./test_runner.sh "$ENVIRONMENT"
   cd -
done
