## Backend of Campiste Reservation

## Local Development
* TheMaven 3.5
* Java 8
* Spring Boot: https://spring.io/projects/spring-boot

# Check out repository

```bash
git clone https://github.com/vvicario/reservation.git
```

## Installation
Run `./mvnw clean install`.

## Deploying
Run `ReservationApplication` from your favorite IDE. Alternatively, run `./mvnw clean package` to generate an campsite-0.1.0.jar, and
then look in your target directory and run `java -jar campsite-0.1.0.jar`. To verify your deployment,
issue a GET to localhost:8080/reservation/available This find out when the campsite is available.
