# iCommerce service
This is a Java Spring boot backend service and handle some tasks:
- manage the product list: search, add, update, delete the product
- customer add/remove product to/from cart

## Prerequisites 
1. Java version >= 11
2. Maven
3. Database
- MySQL version 8
   spring.datasource.url=jdbc:mysql://localhost:3306/icommerce
   spring.datasource.username=root
   spring.datasource.password=icommerce
- Or can use H2 database for testing purpose
## Architecture
![Alt text](diagram/architecture.png?raw=true)

- We can scale out for icommerce service by add new instance service and put these services behind a load balancer
- We can add Order service to handle the order management
- We can add other services for the purpose notification service, report ...
- The communication between these services use message broker.

## DB schema
![Alt text](diagram/database.PNG?raw=true)

## Run application
- run application first time for initialize the tables schema
mvn spring-boot:run
- run sql sript data.sql to insert test data
- stop and run application 2nd time to test 
- run api via postman: import postman collecton at \diagram\icommerce.postman_collection.json
- list of api: http://localhost:8080/swagger-ui/
### Unit Test Coverage
- 100% coverage for Controller & Service layers
- run: mvn clean install
- Check the coverage report from Jacoco at: \target\site\jacoco\index.html
### Documentation 
The swagger documentation can be found at http://localhost:8080/swagger-ui/

### Health checking
Call actuator api for health cheking: http://localhost:8091/actuator

### Security 
The application use Spring Security and mock the token details for demo purpose. 
We can change the class JWTAuthenticationManager to implement the security checking

### Next action
After customers add the product to cart, they can create an Order and deduct the quantity of existing Product

