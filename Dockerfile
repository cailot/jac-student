# Use an official Tomcat base image
FROM tomcat:9.0.89-jdk17

# Set the timezone to Australia/Melbourne
ENV TZ=Australia/Melbourne
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

#ENV CATALINA_OPTS="-Xms2048m -Xmx8192m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+ParallelRefProcEnabled -XX:G1HeapRegionSize=32m"
ENV CATALINA_OPTS="-XX:+ParallelRefProcEnabled"

# Set the working directory inside the container
WORKDIR /usr/local/tomcat

# Remove the default ROOT application
RUN rm -rf webapps/ROOT

# Copy the WAR file to the webapps directory
COPY target/ROOT.war webapps/ROOT.war

# Expose the port Tomcat is running on
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]