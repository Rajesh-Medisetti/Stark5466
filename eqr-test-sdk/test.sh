#!/usr/bin/env bash

set -e

mvn clean test -Dspring.profiles.active=ci
mvn jacoco:report

if [[ $ENVIRONMENT = "ci" ]]
then
  python3 coveragelimits.py
fi
