FROM java:8

MAINTAINER Nathan Zimmerman, npzimmerman@gmail.com

ARG ZKPORT
ENV ZKPORT ${ZKPORT:-2181}

ARG PASS
ENV PASS ${PASS:-password}

ARG INSTANCENAME
ENV INSTANCENAME ${INSTANCENAME:-AccumuloInstance}

ARG USERNAME
ENV USERNAME ${USERNAME:-root}

EXPOSE ${ZKPORT}
EXPOSE 50095

COPY target/scala-2.11/miniAccumuloCluster-assembly-0.1.0.jar /opt/app/miniAccumuloCluster-assembly-0.1.0.jar

CMD java -jar /opt/app/miniAccumuloCluster-assembly-0.1.0.jar
