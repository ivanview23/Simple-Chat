all: clean package run

package:
	mvn -f pom.xml package

run:
	java -jar target/SocketServer-1.0-SNAPSHOT.jar --port=8081

clean:
	mvn -f ./pom.xml clean