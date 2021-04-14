#!/usr/bin/env bash

docker-compose down
rm -rf data
docker system prune --force
docker volume ls "$(docker volume ls -qf dangling=true)" 2>/dev/null
