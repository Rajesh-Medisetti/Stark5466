#!/usr/bin/env bash

set -e

CI=true yarn test --coverage --coverageReporters="json-summary"

if [[ $ENVIRONMENT = "ci" ]]
then
  python3 coveragelimits.py
fi
