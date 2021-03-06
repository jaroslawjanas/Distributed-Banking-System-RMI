rm -rf "..out/"
mkdir -p "../out/classes/client"
mkdir -p "../out/classes/server"

javac ../src/server/*.java ../src/server/errors/*.java ../src/client/*.java ../src/client/errors/*.java -d ../out/classes/
echo "--------------------------------------------------"
echo "Finished compiling"
echo "--------------------------------------------------"