all: clean package run

package:
	mvn -f pom.xml package

run:
	clear && java -jar target/SocketClient-1.0-SNAPSHOT.jar --port=8081

clean:
	mvn -f ./pom.xml clean