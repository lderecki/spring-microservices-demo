cd .
echo "Cleaning up"
docker compose down
docker image rm  (docker image ls -a -q)

#commment out if want to preserve volumes
docker volume rm (docker volume ls -q)

echo "Cleaning up - DONE"


echo "Building eureka"
cd eureka-server
mvn clean install -DskipTests
docker image build -t eureka-server .
cd ..
echo "Building eureka - DONE"

echo "Building dict-service"
cd dict-service
mvn clean install -DskipTests
docker image build -t dict-service .
cd ..
echo "Building dict-service - DONE"

echo "Building crud-service"
cd crud-service
mvn clean install -DskipTests
docker image build -t crud-service .
cd ..
echo "Building crud-service - DONE"

echo "Building api-gateway"
cd api-gateway
mvn clean install -DskipTests
docker image build -t api-gateway .
cd ..
echo "Building api-gateway - DONE"

echo "Building oauth2-acs"
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
cd oauth2-acs
mvn clean install -DskipTests
docker image build -t oauth2-acs .
$env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_202"
cd ..
echo "Building oauth2-acs - DONE"