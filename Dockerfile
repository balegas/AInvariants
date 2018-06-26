FROM openjdk:8-jre

MAINTAINER Valter Balegas <v.sousa@campus.fct.unl.pt>

VOLUME /usr/share/AInvariants/client-output

#ENV CASSANDRA_ENDPOINTS = $CASSANDRA_ENDPOINTS

# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/AInvariants/AInvariants-1.jar
ADD config/ /usr/share/AInvariants/config

WORKDIR /usr/share/AInvariants/

ENV CASSANDRA_DC="dc1"
ENV CASSANDRA_ENDPOINTS="node01"

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/AInvariants/AInvariants-1.jar"]


