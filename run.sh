#!/usr/bin/env bash

source .env

set -e

EQR_TEST_SDK_DOCKER_IMAGES=(eqr-test-sdk)

EQR_TEST_SDK_DEPLOYABLES=(mongo eqr-test-sdk)

build_docker_image() {
    arr=("$@")
    for image in "${arr[@]}"
    do
        docker-compose build "${image}"
    done
}

wait_container_start() {
    service_name="${1}"
    COMPOSE_HTTP_TIMEOUT=120 docker-compose up -d "${service_name}"
    sleep 3;
    until [[ "$(docker inspect -f '{{.State.Running}}' "${PROJECT}_${service_name}")" = "true" ]]; do
        echo .
        sleep 1;
    done;
}

start_docker_container() {
    arr=("$@")
    for deployable in "${arr[@]}"
    do
        if [[ ! "$(docker ps -q -f name="${deployable}")" ]]; then
            if [[ "$(docker ps -aq -f status=exited -f name="${PROJECT}_${deployable}")" ]]; then
                docker rm "${PROJECT}_${deployable}"
            fi
            wait_container_start "${deployable}"
        fi
    done
}

PS3='What do you want to run? '
options=("Entire Stack" "eqr-test-sdk" "Quit")
select opt in "${options[@]}"
do
    case $opt in
        "Entire Stack")
            build_docker_image "${EQR_TEST_SDK_DOCKER_IMAGES[@]}"
            start_docker_container "${EQR_TEST_SDK_DEPLOYABLES[@]}"
            break;
            ;;
        "eqr-test-sdk")
            build_docker_image "${EQR_TEST_SDK_DOCKER_IMAGES[@]}"
            start_docker_container "${EQR_TEST_SDK_DEPLOYABLES[@]}"
            break;
            ;;
        "Quit")
            exit;
            ;;
        *) echo "invalid option $REPLY";;
    esac
done

docker-compose logs -f -t
