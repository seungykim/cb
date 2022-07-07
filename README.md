# Circuit Breaker Demo

## Project Set up

* Language: Java 17
* Runtime: JRE/JVM
* Project: Gradle 7.4.2 (gradle wrapper)
* Framework: Spring Boot 2.7.1
* Circuit Breaker Library: Resilience4j 1.7.2


## How to Build

```
$ ./gradlew clean build
```

## How to Run

```
$ ./gradlew bootRun
```

## How to Invoke End points

```
# To invoke end point, regular interaction
$ curl "http://localhost:8080/albums"
[
  {
    "userId": 1,
    "id": 1,
    "title": "main data 1"
  },
  {
    "userId": 2,
    "id": 2,
    "title": "main data 2"
  },
  {
    "userId": 3,
    "id": 3,
    "title": "main data 3"
  },
  {
    "userId": 4,
    "id": 4,
    "title": "main data 4"
  },
  {
    "userId": 5,
    "id": 5,
    "title": "main data 5"
  }
]


# To invoke end point, and simulate the "external call exception throw" case
$ curl "http://localhost:8080/albumsWithException?throwExceptionParam=true"
[
  {
    "userId": 1,
    "id": 1,
    "title": "fallback data 1"
  },
  {
    "userId": 2,
    "id": 2,
    "title": "fallback data 2"
  }
]

# To invoke end point, with a delay seconds
$ curl "http://localhost:8080/albumsWithDelay?waitSecParam=3"
[
  {
    "userId": 1,
    "id": 1,
    "title": "fallback data 1"
  },
  {
    "userId": 2,
    "id": 2,
    "title": "fallback data 2"
  }
]

```

## How to check the circuit breaker is working

The returned payload will indicate where the source of the data is.

* `main data` comes from the main data provider.
* `fallback data` comes from the fallback/secondary data provider.

