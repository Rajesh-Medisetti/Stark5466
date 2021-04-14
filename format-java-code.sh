#!/usr/bin/env bash

source .env

set -e

IFS=', ' read -r -a arr <<< "$JAVA_BASED_APPLICATIONS"

for i in "${arr[@]}"
do
   cd "$i"
   mvn com.coveo:fmt-maven-plugin:format
   cd -
done
