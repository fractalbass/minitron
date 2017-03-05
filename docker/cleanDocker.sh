#!/bin/bash

echo "stopping and removing ALL docker containers"
for dockerid in $(docker ps -qa); do
    echo -n "stopping "
        docker stop $dockerid
    sleep 2
    echo -n "removing "
    docker rm $dockerid
done

echo "cleaning out volumes"
docker volume rm $(docker volume ls -q)

echo "cleaning out dockertest networks"
docker network rm $(docker network ls -qf 'name=dockertest*')
