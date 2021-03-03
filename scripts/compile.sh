rm -rf "..out/"
mkdir -p "../out/classes/client"
mkdir -p "../out/classes/server"

javac ../src/server/*.java -d ../out/classes/
javac ../src/client/*.java -d ../out/classes/ -cp ../src/server/*.java
echo "Finished compiling"
