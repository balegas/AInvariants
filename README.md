# AInvariants


TODO: Add description



### How to run (work in progress)

Need to create docker compose file with attached volumes to store output. Here are the commands you can use to set-up a two DC deployment and run the client.

First, get pull the client image:
```sh
$ docker pull balegas/ainvariants
```

Create network:
```sh
$ docker network create cassandra-network
```

Start DC1:
```sh
$ docker run --name node01 --network cassandra-network \
 -p 7000:7000 -p 7001:7001 -p 7199:7199  -p 9042:9042  -p 9160:9160 \
 -e CASSANDRA_DC=dc1 \
 -d launcher.gcr.io/google/cassandra3
 
$ docker logs -f node01

```

Start DC2:
```sh
$ docker run --name node02 --network cassandra-network \
 -e CASSANDRA_SEEDS=node01 \
 -e CASSANDRA_DC=dc2 \
 -d launcher.gcr.io/google/cassandra3
 
$ docker logs -f node02
```

Run client (-i initialize, -p printstate):
```sh
docker run --name initializer --network cassandra-network -e EXECUTOR_ENDPOINTS=node01,node02 balegas/ainvariants:latest

