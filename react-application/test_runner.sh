#!/usr/bin/env bash

source ../.env

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

export RAND=$(date +%s)

docker build -t tests-react-application-"${PROJECT}"-"${RAND}" -f Dockerfile.ci .

docker run --rm \
 -e COVERAGE_LIMITS_SPREADSHEET_SECRET="$COVERAGE_LIMITS_SPREADSHEET_SECRET" \
 -e COVERAGE_LIMITS_SPREADSHEET_WORKSHEET="${PROJECT}/react-application" \
 -e GIT_BRANCH="$GIT_BRANCH" \
 -e ENVIRONMENT="$ENVIRONMENT" \
 tests-react-application-"${PROJECT}"-"${RAND}"
