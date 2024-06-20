# Use an official Tomcat base image
FROM tomcat:9.0.89-jdk17

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