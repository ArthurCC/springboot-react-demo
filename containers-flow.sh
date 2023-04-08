# DOCKER FLOW
docker network create springboot-react-demo-network
# DB
docker run --name springboot-react-demo-db -e POSTGRES_PASSWORD=postgres -p 5432:5432 --network springboot-react-demo-network -d postgres:15-alpine
docker exec -it springboot-react-demo-db sh
psql -U postgres
CREATE DATABASE springbootreactdemo OWNER postgres;

# APP
mvn clean package
docker build -t arthuru44/springboot-react-demo:0.0.1-SNAPSHOT .
docker run -p 8080:8080 -d --network springboot-react-demo-network --name springboot-react-app arthuru44/springboot-react-demo:0.0.1-SNAPSHOT