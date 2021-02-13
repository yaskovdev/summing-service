# Summing Service

Demo service that sums up integer numbers.

To submit a number use the next cURL command:
```shell
curl -H "Content-Type: application/json" -d 1 http://localhost:8080/numbers
```

To calculate the sum use the next cURL command:
```shell
curl -H "Content-Type: application/json" -d end http://localhost:8080/numbers
```

Example:
```shell
curl -H "Content-Type: application/json" -d 1 http://localhost:8080/numbers
curl -H "Content-Type: application/json" -d 2 http://localhost:8080/numbers
curl -H "Content-Type: application/json" -d 3 http://localhost:8080/numbers
curl -H "Content-Type: application/json" -d end http://localhost:8080/numbers
```