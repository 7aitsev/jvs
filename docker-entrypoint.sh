#!/bin/sh

git clone $1 && dir=$(ls) && cd "$dir"

mkdir lib/ && \
wget --quiet -P lib/ "http://central.maven.org/maven2/com/google/code/gson/gson/2.8.2/gson-2.8.2.jar" && \
javac -sourcepath src/ src/Main.java -cp lib/gson-2.8.2.jar -d . && \
java -classpath lib/gson-2.8.2.jar:.:service -Djava.util.logging.config.file=logging.properties Main
