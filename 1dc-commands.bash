#!/bin/bash

POSITIONAL=()
while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    #Delete cluster nodes
    -d|--delete)
        rm -fr output/node01/ output/node02/ output/node03/
        docker rm -f node01 node02 node03
    shift # past argument
    shift # past value
    ;;
    #Delete all nodes
    -D|--deleteAll)
        rm -fr output/node01/ output/node02/ output/node03/ output/client/        
        docker rm -f node01 node02 node03 cassandra-client cassandra-client-initializer
        shift # past argument
        shift # past value
        exit
    ;;
    #Run client
    -c|--client)
        docker rm -f cassandra-client
        docker run --name cassandra-client -v $(pwd)/output/client:/usr/share/AInvariants/output \
            --network cassandra-network \
            -e EXECUTOR_ENDPOINTS=node01,node02,node03 \
            balegas/ainvariants:latest
        shift # past argument
        exit
    ;;
    #Run client initializer
    -i|--initializer)
        docker rm -f cassandra-client-initializer
        docker run --name cassandra-client-initializer --network cassandra-network \
            -e EXECUTOR_ENDPOINTS=node01,node02,node03 \
            -e EXECUTOR_CONSISTENCY=QUORUM \
            balegas/ainvariants:latest -i
        shift # past argument
        exit
    ;;
esac
done
set -- "${POSITIONAL[@]}" # restore positional parameters

mkdir -p output/node01/ output/node02/ output/node03/

docker run --name node01 --network cassandra-network \
 -v $(pwd)/config:/etc/cassandra/config \
 -v $(pwd)/output/node01:/usr/share/AInvariants/output/ \
 -e CASSANDRA_SEEDS=node01 \
 -e CASSANDRA_DC=dc1 \
 -e CASSANDRA_RACK=rack01 \
 -e CASSANDRA_CLUSTER_NAME="Cluster" \
 -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch \
 -d launcher.gcr.io/google/cassandra3 -Dcassandra.metricsReporterConfigFile=/etc/cassandra/config/metrics.yml

sleep 5

docker run --name node02 --network cassandra-network \
 -v $(pwd)/config:/etc/cassandra/config \
 -v $(pwd)/output/node02:/usr/share/AInvariants/output/ \
 -e CASSANDRA_SEEDS=node01 \
 -e CASSANDRA_DC=dc1 \
 -e CASSANDRA_RACK=rack01 \
 -e CASSANDRA_CLUSTER_NAME="Cluster" \
 -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch \
 -d launcher.gcr.io/google/cassandra3 -Dcassandra.metricsReporterConfigFile=/etc/cassandra/config/metrics.yml

sleep 60

docker run --name node03 --network cassandra-network \
 -v $(pwd)/config:/etc/cassandra/config \
 -v $(pwd)/output/node03:/usr/share/AInvariants/output/ \
 -e CASSANDRA_SEEDS=node01 \
 -e CASSANDRA_DC=dc1 \
 -e CASSANDRA_RACK=rack01 \
 -e CASSANDRA_CLUSTER_NAME="Cluster" \
 -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch \
 -d launcher.gcr.io/google/cassandra3 -Dcassandra.metricsReporterConfigFile=/etc/cassandra/config/metrics.yml