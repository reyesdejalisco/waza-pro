#!/usr/bin/env sh
# Minimal gradle wrapper launcher
set -e
DIR=$(cd "$(dirname "$0")" && pwd)
if [ ! -f "$DIR/gradle/wrapper/gradle-wrapper.jar" ]; then
  echo "Descargando gradle-wrapper..."
  mkdir -p "$DIR/gradle/wrapper"
  curl -sL https://github.com/gradle/gradle/raw/master/gradle/wrapper/gradle-wrapper.jar -o "$DIR/gradle/wrapper/gradle-wrapper.jar" || wget -q https://services.gradle.org/distributions/gradle-8.5-bin.zip -O /tmp/gradle.zip
fi
exec java -jar "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
