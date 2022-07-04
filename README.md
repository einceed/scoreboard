# Getting Started

To run this SpringBoot application you don't need to do any special moves.
You also may want to use different DB for storing events and scores. To do
so you need to change properties which you can find in `application.properties`
file. The properties you should change
```properties
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
spring.jpa.database-platform=
```
and run  in your IDE.

OR you can also use the following command to run the application:
`mvn clean package`
then run this command `java -jar target/itask-0.0.1-SNAPSHOT.jar` to start the service.
By default service will run on port 8080.

#Tests

1. first you should create an event by this API:

```http request
POST http://localhost:8080/api/event?name=team 1 vs team 2

query params: name - name of the event
it's required parameter, should consist from 2 teams with names separated by ` vs `
example: `team 1 vs team 2`

response:
{
  "id": 1,
  "name": "team 1 vs team 2"
}
status code: 
  201 - if event was created successfully
  400 - if event was not created because of invalid name
  500 - if event was not created because of some internal error 
```

2. you can get the events list by this API:

```http request
GET http://localhost:8080/api/events

response:
[
  {
    "id": 1,
    "name": "team 1 vs team 2"
  }
]

status code: 200
```

3. now you can post the scores by eventId by this API:

```http request
POST http://localhost:8080/api/event/1/score?minute=10&score=2-3

query params: minute - number of minute when the score was updated, it's required parameter, should be integer
              score - score of the event, it's required parameter, should be string with format `2-3`
              
status code:
  201 - if the score was updated successfully
  400 - if the event was not found
  400 - if the score was not valid
```

4. you can get the latest score of the event by this API:
    
```http request
GET http://localhost:8080/api/event/1/score

response:
{
  "minute": 10,
  "score": "2-3"
}

status code: 200
```

5. you can get event by id by this API:
    
```http request
GET http://localhost:8080/api/event/1

response:
{
  "id": 1,
  "name": "team 1 vs team 2",
  "scores": [
    {
      "minute": 10,
      "score": "2-3"
    }
  ]
}

status code: 200
```

6. you can find page where you will be notifying about all new scores by event
path to page:
```http request
localhost:8080/notification
```
in that page you can select specific event in dropdown and subscribe to it.