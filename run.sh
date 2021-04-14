#!/usr/bin/env bash

source .env

set -e

JAVA_APPLICATION_DOCKER_IMAGES=(java-application)
REACT_APPLICATION_DOCKER_IMAGES=(react-application)

JAVA_APPLICATION_DEPLOYABLES=(mongo java-application)
REACT_APPLICATION_DEPLOYABLES=(react-application)

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
options=("Entire Stack" "java-application" "react-application" "Quit")
select opt in "${options[@]}"
do
    case $opt in
        "Entire Stack")
            build_docker_image "${JAVA_APPLICATION_DOCKER_IMAGES[@]}" "${REACT_APPLICATION_DOCKER_IMAGES[@]}"
            start_docker_container "${JAVA_APPLICATION_DEPLOYABLES[@]}" "${REACT_APPLICATION_DEPLOYABLES[@]}"
            break;
            ;;
        "java-application")
            build_docker_image "${JAVA_APPLICATION_DOCKER_IMAGES[@]}"
            start_docker_container "${JAVA_APPLICATION_DEPLOYABLES[@]}"
            break;
            ;;
        "react-application")
            build_docker_image "${REACT_APPLICATION_DOCKER_IMAGES[@]}"
            start_docker_container "${REACT_APPLICATION_DEPLOYABLES[@]}"
            break;
            ;;
        "Quit")
            exit;
            ;;
        *) echo "invalid option $REPLY";;
    esac
done

docker-compose logs -f -t
