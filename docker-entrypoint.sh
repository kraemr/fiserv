#!/bin/sh
set -e


echo "pw $MYSQL_PASSWORD"
echo "mysql $MYSQL_USER"



# Initialize MariaDB if needed
if [ ! -d /var/lib/mysql/mysql ]; then
    echo "Initializing MariaDB..."
    chown -R mysql:mysql /var/lib/mysql
    mysql_install_db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
fi

echo "Starting MariaDB..."
mysqld --user=mysql --bind-address=0.0.0.0 --port=3306 --console &

sleep 4



# Run bootstrap SQL if not done yet
if [ ! -f /var/lib/mysql/.init_done ]; then
    echo "Bootstrapping MariaDB user and database..."

   mysql --host=localhost --user=root <<-EOSQL
    CREATE DATABASE IF NOT EXISTS \`${MYSQL_DATABASE}\`;
    CREATE USER IF NOT EXISTS '${MYSQL_USER}'@'%' IDENTIFIED BY '${MYSQL_PASSWORD}';
    GRANT ALL PRIVILEGES ON \`${MYSQL_DATABASE}\`.* TO '${MYSQL_USER}'@'%';
    CREATE USER IF NOT EXISTS '${MYSQL_USER}'@'localhost' IDENTIFIED BY '${MYSQL_PASSWORD}';
    GRANT ALL PRIVILEGES ON \`${MYSQL_DATABASE}\`.* TO '${MYSQL_USER}'@'localhost';
    FLUSH PRIVILEGES;
EOSQL

    touch /var/lib/mysql/.init_done
fi

mysql -h 127.0.0.1 -u myuser -p"${MYSQL_PASSWORD}" --verbose -e "SHOW DATABASES;"


# Start the Spring Boot app
echo "Starting Spring Boot application..."
exec "$@"
