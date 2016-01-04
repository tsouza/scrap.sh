FROM java:8

WORKDIR /opt/${artifactId}/
ADD ${jarFilename} /opt/${artifactId}/

EXPOSE 8080 8081 11211 47100 47400 47500 48100

CMD /usr/bin/java \
  -server -Xms512m -Xmx1g -XX:+AggressiveOpts \
  -Dspring.profiles.active=production \
  -jar ${jarFilename}
