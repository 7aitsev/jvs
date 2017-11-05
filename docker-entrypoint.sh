#!/bin/sh
OUTDIR=out

git clone $1
dir=$(ls)
mkdir "$dir"/"$OUTDIR" && cd "$dir"/"$OUTDIR"

javac -sourcepath ../src/ ../src/Main.java -cp ../lib/gson-2.8.2.jar -d .
java -classpath ../lib/gson-2.8.2.jar:.:service Main
