CONFIG_DIR='/Users/vbalegas/workspace/AInvariants/config'
USER=vbalegas
SERVER_WORKDIR='/home/vbalegas'
DOCKER_RM_ALL="docker rm -f \$(docker container ps -a -q)"
DOCKER_RM_NODE="docker rm -f node"


POSITIONAL=()
while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    -n|--nodes)
        IFS=',' read -r -a NODES <<< "$2"
        shift 
        shift
    ;;
    -d|--dc-name)
        DC_NAME=$2
    shift
    shift
    ;;
    -c|--copy-cofig)
        DOCKER_CMD="docker volume rm \$(docker volume ls -q)"
        for NODE in ${NODES[@]}
        do
            :
            ssh -o "StrictHostKeyChecking no" $USER@$NODE "sudo rm -fr $SERVER_WORKDIR/config"
            ssh -o "StrictHostKeyChecking no" $USER@$NODE $DOCKER_RM_ALL
            ssh -o "StrictHostKeyChecking no" $USER@$NODE $DOCKER_CMD
            scp -o "StrictHostKeyChecking no" -r $CONFIG_DIR $USER@$NODE:$SERVER_WORKDIR
        done  
    shift
    ;;
    -s|--seed-nodes)
        SEED_NODES=$2
    shift
    shift
    ;;
esac
done
set -- "${POSITIONAL[@]}" # restore positional parameters


for NODE in ${NODES[@]}
do
    :
    echo "NODE ${NODE}"
    ssh -o "StrictHostKeyChecking no" $USER@$NODE $DOCKER_RM_NODE
    DOCKER_CMD="docker run \
     --name node \
     -p 7000:7000 \
     -p 7001:7001 \
     -p 7199:7199 \
     -p 9042:9042 \
     -p 9160:9160 \
     -v $SERVER_WORKDIR/output:/var/lib/cassandra/ \
     -v $SERVER_WORKDIR/config:/etc/cassandra/config \
     -e CASSANDRA_BROADCAST_ADDRESS=${NODE} \
     -e CASSANDRA_SEEDS=$SEED_NODES \
     -e CASSANDRA_DC=$DC_NAME \
     -e CASSANDRA_RACK=rack01 \
     -e CASSANDRA_CLUSTER_NAME='Cluster' \
     -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch \
     -d launcher.gcr.io/google/cassandra3 -Dcassandra.metricsReporterConfigFile=config/metrics.yml"
    ssh -o "StrictHostKeyChecking no" $USER@$NODE $DOCKER_CMD
    #echo $DOCKER_CMD    
done    

exit 
