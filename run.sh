#!/usr/bin/env bash

source .env

set -e

E2E_SUITE_DOCKER_IMAGES=(e2e-suite)
PERF_SUITE_DOCKER_IMAGES=(perf-suite)

E2E_SUITE_DEPLOYABLES=(mongo e2e-suite)
PERF_SUITE_DEPLOYABLES=(mongo perf-suite)

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
options=("Entire Stack" "e2e-suite" "per-suite" "Quit")
select opt in "${options[@]}"
do
    case $opt in
        "Entire Stack")
            build_docker_image "${E2E_SUITE_DOCKER_IMAGES[@]}" "${PERF_SUITE_DOCKER_IMAGES[@]}"
            start_docker_container "${E2E_SUITE_DEPLOYABLES[@]}" "${PERF_SUITE_DEPLOYABLES[@]}"
            break;
            ;;
        "e2e-suite")
            build_docker_image "${E2E_SUITE_DOCKER_IMAGES[@]}"
            start_docker_container "${E2E_SUITE_DEPLOYABLES[@]}"
            break;
            ;;
        "perf-suite")
            build_docker_image "${PERF_SUITE_DOCKER_IMAGES[@]}"
            start_docker_container "${PERF_SUITE_DEPLOYABLES[@]}"
            break;
            ;;
        "Quit")
            exit;
            ;;
        *) echo "invalid option $REPLY";;
    esac
done

docker-compose logs -f -t
