# obsidiam-challenge

## High-level architecture

We have a server application which has dependency on a MySQL database and exposes a RESTful API.

## Build and run the backend application

Clone GitHub repository locally:

`https://github.com/joaquinrojkind/obsidiam-challenge`

From the terminal window in the project's root directory execute the following command:

`docker-compose up &`

This will run a MySQL database on docker.

Compile the project

`mvn clean install`

Run the server, it uses port 8090, make sure it's not in use.

`mvn spring-boot:run`

## Verify the database

With a MySQL database client such as Sequel Pro, connect to the database and verify the table data.
Connection params are as follows:

- Host: `127.0.0.1`
- Port: `3306`
- Username: `root`
- Password: `123456`

## Further testing with Postman

Use the Postman collection in `src/main/resources/postman` in order to test the following:

- Create exchange order endpoint.
- Check exchange order status endpoint.

## Automated tests

### Unit tests

PËNDING

Implement unit tests on the service classes

### Integration tests

PENDING

Implement integration tests to test the API endpoints 

## Questions from the challenge

1) Why did I choose this architectural style on the server application?

I implemented a classical three-tier architecture where we have the modules of API, services, and repositories. Modules allow separation of concerns and a straight forward execution flow. We could use an hexagonal architecture instead which has many advantages but for such a small application I decided to keep it simple.

2) What would I do differently if the system grew up to 10000 transactions per second?

It seems to me that the basic approach would be to scale the whole infrastructure horizontally (assuming a Kubernetes cluster or similar), which means adding replicas or nodes of the server-side applications and probably the database as well.
