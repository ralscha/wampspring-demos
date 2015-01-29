A sample application that demonstrates how to write a Java client with Spring and wampspring that connects to a Spring Boot server.

Start the server
     1. ```cd server```
     2. ```mvn spring-boot:run```

### CALL	 
Start the Call client. This client sends CALL messages to the server.
     1. ```cd client```
	 2. ```mvn exec:java -Dexec.mainClass="ch.rasc.wampspring.demo.client.CallClient"```

	 
### PUB/SUB
	 
Start the Subscriber client. This client sends a SUBSCRIBE message on the destination /test/myqueue to the server.
     1. ```cd client```
	 2. ```mvn exec:java -Dexec.mainClass="ch.rasc.wampspring.demo.client.Subscriber"```

Start the Publisher client. This client publishes messages to the destination /test/myqueue. The Subscriber receives these messages.
     1. ```cd client```
	 2. ```mvn exec:java -Dexec.mainClass="ch.rasc.wampspring.demo.client.Publisher"``` 