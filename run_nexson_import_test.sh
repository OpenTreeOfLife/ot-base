CLASSPATH="/home/cody/phylo/ot-base/src/main/java/:/home/cody/.m2/repository/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar:/home/cody/.m2/repository/org/opentree/jade/0.0.1-SNAPSHOT/jade-0.0.1-SNAPSHOT.jar"

#"/home/cody/.m2/repository/org/opentree/ot-base/0.0.1-SNAPSHOT/ot-base-0.0.1-SNAPSHOT.jar"

echo compiling
rm src/main/java/org/opentree/nexson/io/*.class
javac -cp $CLASSPATH src/main/java/org/opentree/nexson/io/NexsonSource.java

echo running
java -cp $CLASSPATH org.opentree.nexson.io.NexsonSource $1
