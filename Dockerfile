FROM openjdk:8-jre
MAINTAINER Valter Balegas <v.sousa@campus.fct.unl.pt>

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/AInvariants/AInvariants-1.jar"]

# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/AInvariants/AInvariants-1.jar