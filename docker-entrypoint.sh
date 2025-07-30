#!/bin/sh
set -e

# # Initialize MariaDB if needed
# if [ ! -d /var/lib/mysql/mysql ]; then
#     echo "Initializing MariaDB..."
#     chown -R mysql:mysql /var/lib/mysql
#     mysql_install_db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
# fi

# if [ ! -f /var/lib/mysql/.init_done ]; then
#     echo "Bootstrapping MariaDB user and database..."
#     mysqld --user=mysql --datadir=/var/lib/mysql --skip-networking --bootstrap <<-EOSQL
#         CREATE DATABASE IF NOT EXISTS \`${MYSQL_DATABASE}\`;
#         CREATE USER IF NOT EXISTS '${MYSQL_USER}'@'%' IDENTIFIED BY '${MYSQL_PASSWORD}';
#         GRANT ALL PRIVILEGES ON \`${MYSQL_DATABASE}\`.* TO '${MYSQL_USER}'@'%';
#         GRANT ALL ON *.* TO 'root'@'%' IDENTIFIED BY '${MYSQL_ROOT_PASSWORD}' WITH GRANT OPTION;
#         FLUSH PRIVILEGES;
# EOSQL

#     touch /var/lib/mysql/.init_done
# fi

# Start MariaDB in background
echo "Starting MariaDB..."
mysqld --user=mysql --console &

# Wait for MariaDB to fully start before running the app
echo "Waiting for MariaDB to be ready..."
sleep 10

# Start the Spring Boot app
echo "Starting Spring Boot application..."
exec "$@"
