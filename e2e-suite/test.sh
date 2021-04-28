#!/usr/bin/env bash

set -e

mvn clean install -s settings.xml -DskipTests
