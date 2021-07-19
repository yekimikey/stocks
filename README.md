# stocks

Spring Initializr used for generating base project https://start.spring.io/

This project uses java 11

To run this project locally:
clone the project
$ mvn clean install
$ java -jar /apps/stocks/target/stocks-0.0.1-SNAPSHOT.jar


Commands are as follows:
to add a value to your watched stock list:
curl -X PUT http://localhost:8080/resource/stocks/add/watched/{StockSymbol}

to remove a value from your watched stock list:
curl -X PUT http://localhost:8080/resource/stocks/remove/watched/{StockSymbol}

to view current watched stock information:
curl http://localhost:8080/resource/stocks/view/watched/stocks/current

to view historical watched stock information:
curl http://localhost:8080/resource/stocks/view/watched/stocks/historical

