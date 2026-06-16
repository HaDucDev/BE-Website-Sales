#!/bin/sh
set -eu

MYSQL_HOST="${MYSQL_HOST:-mysqldb}"
MYSQL_PORT="${MYSQL_PORT:-3306}"
MYSQL_DATABASE="${MYSQL_DATABASE:-hdshopdb}"
MYSQL_USER="${MYSQL_USER:-root}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-123456}"
SEED_FILE="/seed/seed-hdshopdb.sql"

mysql_exec() {
    mysql -h"${MYSQL_HOST}" -P"${MYSQL_PORT}" -u"${MYSQL_USER}" -p"${MYSQL_PASSWORD}" "$@"
}

echo "Waiting for database '${MYSQL_DATABASE}' and Hibernate-created tables..."

attempt=0
max_attempts=120

while :; do
    table_count="$(mysql_exec -N -B -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='${MYSQL_DATABASE}' AND table_name IN ('role','category','supplier','product','user','cart');" 2>/dev/null || echo 0)"

    if [ "${table_count}" = "6" ]; then
        echo "Required tables are ready."
        break
    fi

    attempt=$((attempt + 1))
    if [ "${attempt}" -ge "${max_attempts}" ]; then
        echo "ERROR: Timed out waiting for Hibernate tables in '${MYSQL_DATABASE}'." >&2
        echo "Make sure the Spring Boot backend can connect to MySQL and start successfully." >&2
        exit 1
    fi

    echo "Tables not ready yet (${table_count}/6). Retry ${attempt}/${max_attempts}..."
    sleep 3
done

existing_products="$(mysql_exec -D "${MYSQL_DATABASE}" -N -B -e "SELECT COUNT(*) FROM product WHERE is_delete = 0;" 2>/dev/null || echo 0)"

if [ "${existing_products}" = "0" ]; then
    echo "No product data found. Importing seed file '${SEED_FILE}'..."
    mysql_exec "${MYSQL_DATABASE}" < "${SEED_FILE}"
    echo "Seed data imported successfully."
else
    echo "Existing product data found (${existing_products} rows). Skip seed import."
fi

