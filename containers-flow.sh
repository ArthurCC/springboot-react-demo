# DOCKER FLOW
docker network create springboot-react-demo-network
# DB
docker run --name springboot-react-demo-db -p 5432:5432 -v springboot-react-demo-data:/var/lib/postgresql/data --network springboot-react-demo-network -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=springbootreactdemo -d postgres:15-alpine

# APP
docker rmi arthuru44/springboot-react-demo:0.0.1-SNAPSHOT
mvn clean package
docker build -t arthuru44/springboot-react-demo:0.0.1-SNAPSHOT .
docker run -p 8080:8080 -d --network springboot-react-demo-network --name springboot-react-demo-app arthuru44/springboot-react-demo:0.0.1-SNAPSHOT