FROM alpine:latest

# Install required packages: bash, OpenJDK, MariaDB
RUN apk update && apk add --no-cache \
    openjdk17 \
    bash \
    mariadb mariadb-client \
    curl \
    git \
    unzip

ENV MYSQL_ROOT_PASSWORD=rootpassword \
    MYSQL_DATABASE=mydatabase \
    MYSQL_USER=myuser \
    MYSQL_PASSWORD=mypassword

RUN mkdir /var/lib/mysql
RUN mkdir -p /run/mysqld && chown -R mysql:mysql /run/mysqld
RUN chown -R mysql:mysql /var/lib/mysql

# Define Gradle version
ENV GRADLE_VERSION=8.5
ENV GRADLE_HOME=/opt/gradle/gradle-${GRADLE_VERSION}
ENV PATH="${GRADLE_HOME}/bin:${PATH}"

# Install Gradle manually
RUN mkdir -p /opt/gradle \
 && curl -fsSL https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle.zip \
 && unzip gradle.zip -d /opt/gradle \
 && ln -s /opt/gradle/gradle-${GRADLE_VERSION}/bin/gradle /usr/bin/gradle \
 && rm gradle.zip

# Copy Spring Boot app
COPY server/ /opt/server/
WORKDIR /opt/server

# Copy and set permissions for MariaDB entrypoint
COPY docker-entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

# Expose both MariaDB and Spring Boot ports
EXPOSE 3306 9888

# Entrypoint: run MariaDB init in background, then Spring Boot app
ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
CMD ["gradle","--no-daemon", "bootRun"]
