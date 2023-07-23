<h1 align="center">Welcome to the Patient Microservice 👋</h1>
<p>
</p>

> The microservice is a part of the Project Mediscreen. It contains all the patients data.

## Versions
- Spring Boot: 3.1.2
- Maven: 3.1.2
- JDK: 17

## Run the app

To launch the app, you can run it locally or use docker.

### Run with docker
Run `docker-compose up -d --build`
This will start the app and the database.
You can check on the configured server address : http://localhost:8083

### Run local

Launch the database :
1. Make sure that you have MySQL installed
2. Open the config/application.properties file in your Spring Boot project.
3. Complete the configuration with your own password
4. Ensure that the MySQL server is running. Connect to your MySQL server using the command-line client: `mysql -u your_username -p`
5. Create the database by executing the following command: `CREATE DATABASE mediscreen_patient`;
This will start the app on the configured server address : http://localhost:3306

Launch the app :
1. Make sure you have the required versions of Java and dependencies installed.
2. Open a terminal or command prompt and navigate to the project directory.
3. Run the following command to build the project and create an executable JAR file:
   ` mvn package`
4. Once the build is successful, you can launch the app using the following command:
   ` java -jar target/patient-0.0.1-SNAPSHOT.jar `
   This will start the app on the configured server address : http://localhost:8083

## Testing

Run the command for testing:
- `mvn test`
The jacoco report will be generated in target/site/index.html


## Endpoints

You can check the endpoints with requirements on Swagger : 
http://localhost:8081/swagger-ui/index.html

## Curls
Note: Make sure the app and the database are running

`curl -d "family=TestNone&given=Test&dob=1966-12-31&sex=F&address=1 Brookside St&phone=100-222-3333" -X POST http://localhost:8081/patient/add`
`curl -d "family=TestBorderline&given=Test&dob=1945-06-24&sex=M&address=2 High St&phone=200-333-4444" -X POST http://localhost:8081/patient/add`
`curl -d "family=TestInDanger&given=Test&dob=2004-06-18&sex=M&address=3 Club Road&phone=300-444-5555" -X POST http://localhost:8081/patient/add`
`curl -d "family=TestEarlyOnset&given=Test&dob=2002-06-28&sex=F&address=4 Valley Dr&phone=400-555-6666" -X POST http://localhost:8081/patient/add`