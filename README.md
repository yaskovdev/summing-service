# Summing Service

Demo service that sums up integer numbers.

## How to run the service locally

The service is tested with:

* Java 11.0.8 (OpenJDK)
* Apache Maven 3.6.3

Before running the service, make sure the `8080` port is free. If it is not, you can specify another port by
adding `server.port` property to the `application.properties`, for example `server.port=8081`.

In order to run the service:

1. Go to the application folder (the folder with the `pom.xml`).
1. Run `mvn clean spring-boot:run` to build and run the service.

You can use `mvn clean test` to run unit tests without building the service.

## How to call the service locally

To submit a number use the next cURL command:

```shell
curl -H "Content-Type: application/json" -d 1 http://localhost:8080/numbers
```

To calculate the sum of all the submitted numbers use the next cURL command:

```shell
curl -H "Content-Type: application/json" -d end http://localhost:8080/numbers
```

## Example

```shell
curl -H "Content-Type: application/json" -d 1 http://localhost:8080/numbers
curl -H "Content-Type: application/json" -d 2 http://localhost:8080/numbers
curl -H "Content-Type: application/json" -d 3 http://localhost:8080/numbers
curl -H "Content-Type: application/json" -d end http://localhost:8080/numbers
```

After sending the example requests, they all should return `6` as a response.
