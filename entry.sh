#!/usr/bin/env bash

set -e

gitleaks --path=. -v --no-git
pre-commit run -a
