#!/usr/bin/env bash

source .env

set -e

cd eqr-test-sdk
mvn clean install
