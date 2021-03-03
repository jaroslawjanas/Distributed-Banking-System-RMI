parentdir="$(dirname "$PWD")"
echo "Parent directory: $parentdir"
CLASSPATH="$parentdir/out/classes" export CLASSPATH
rmiregistry #-J-Djava.rmi.server.codebase="file:$parentdir/out/classes/"
