#!/usr/bin/env bash
set -euo pipefail

# run-fix-with-h2.sh
# Usage: ./run-fix-with-h2.sh
# Runs fix-recipe-type.sql against the H2 file DB used by the app

BASE_DIR="$(cd "$(dirname "$0")" && pwd)"
TOOLS_DIR="$BASE_DIR/tools"
H2_JAR="$TOOLS_DIR/h2.jar"
SQL_FILE="$BASE_DIR/fix-recipe-type.sql"

JDBC_URL="jdbc:h2:file:./data/recipeshare;AUTO_SERVER=TRUE"
USER="sa"
PASS="1234"

# Stop early if SQL file missing
if [ ! -f "$SQL_FILE" ]; then
  echo "ERROR: SQL script not found: $SQL_FILE"
  exit 1
fi

# Warn user
cat <<EOF
This script will run SQL in: $SQL_FILE
Against H2 JDBC URL: $JDBC_URL
User: $USER

IMPORTANT: Make sure the application is NOT running when you execute this script.
If the app is running it may hold file locks and the commands may fail or corrupt the DB.

Press Enter to continue, or Ctrl-C to abort.
EOF
read -r

# Create tools dir
mkdir -p "$TOOLS_DIR"

# Download H2 jar if missing
if [ ! -f "$H2_JAR" ]; then
  echo "Downloading H2 jar to $H2_JAR..."
  H2_URL="https://repo1.maven.org/maven2/com/h2database/h2/2.2.220/h2-2.2.220.jar"
  curl -L -o "$H2_JAR" "$H2_URL"
  echo "Downloaded H2 jar."
fi

# Run RunScript
echo "Running SQL script against H2 DB..."
java -cp "$H2_JAR" org.h2.tools.RunScript -url "$JDBC_URL" -user "$USER" -password "$PASS" -script "$SQL_FILE" -showResults

echo "Done. If the SQL ran successfully, restart your application now."

