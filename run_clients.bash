CONFIG_DIR='/Users/vbalegas/workspace/AInvariants/config'
USER=vbalegas
SERVER_WORKDIR='/home/vbalegas'
DOCKER_RM_NODE="docker rm -f client"
CONSISTENCY="LOCAL_QUORUM"


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
-i|--init)
        ARGS=" -i"
    shift
    ;;
-c|--consistency)
        CONSISTENCY=$2
    shift
    shift
    ;;
-e|--endpoints)
        ENDPOINTS=$2
    shift
    shift
    ;;
-d|--dc-name)
        DC_NAME=$2
    shift
    shift
    ;;
esac
done
set -- "${POSITIONAL[@]}" # restore positional parameters


for NODE in ${NODES[@]}
do
    :
    ssh -o "StrictHostKeyChecking no" $USER@$NODE $DOCKER_RM_NODE
    DOCKER_CMD="docker run \
     --name client \
     -v $SERVER_WORKDIR/:/usr/share/AInvariants/output/ \
     -e EXECUTOR_DC_NAMES=$DC_NAME \
     -e EXECUTOR_CONSISTENCY=$CONSISTENCY \
     -e EXECUTOR_ENDPOINTS=$ENDPOINTS
     -d balegas/ainvariants "$ARGS
    ssh -o "StrictHostKeyChecking no" $USER@$NODE $DOCKER_CMD
    #echo $DOCKER_CMD
    
done    

exit 
