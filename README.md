# Test task "Inside" company - REST API application

This is simple application for creating and storage messages, providing a REST API. 

Authorisation provided by Bearer tokens. Everyone can see all users of application, 
but only authorized user can add messages, and see messages history. 

Users can see only their own messages. 
   
## Run the application
Application containerized in docker container. Link is [here](https://hub.docker.com/repository/docker/32391443/inside_test_app), 
but for work application also require data base. My recommendation is [download docker-compose](https://disk.yandex.ru/d/34isC-T4sEH04Q) file and use:
    
    docker-compose up

# REST API
The REST API to the example app is described below. Application based on 8181 port.


## Get list of Users

### Request

`GET /api/user/`

    curl -i -H 'Accept:application/json' http://localhost:8181/api/user

### Response

    HTTP/1.1 200
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Thu, 21 Apr 2022 15:42:32 GMT

    [{"id":1,"username":"Alexey"},{"id":2,"username":"Petr"},{"id":3,"username":"Ivan"}]


## Create a new User

### Request

`POST /api/user/`

    for unix bash:
    curl -i -X POST http://localhost:8181/api/user -H "Content-Type:application/json" -d '{"username":"Nik", "password":"nikpass"}'
    
    for windows cmd:
    curl -i -X POST http://localhost:8181/api/user -H "Content-Type:application/json" -d "{\"username\": \"Nik\",  \"password\" : \"nikpass\" }" 
### Response

    HTTP/1.1 200
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Thu, 21 Apr 2022 15:51:33 GMT

    {"id":4,"username":"Nik"}


## Get a specific User

### Request

`GET /api/user/id`

    curl -i -H 'Accept:application/json' http://localhost:8181/api/user/1

### Response

    HTTP/1.1 200
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Thu, 21 Apr 2022 15:59:11 GMT

    {"id":1,"username":"Alexey"}
    
    
## Get a non-existent User

### Request

`GET /api/user/id`

    curl -i -H 'Accept:application/json' http://localhost:8181/api/user/100

### Response

    HTTP/1.1 404
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Thu, 21 Apr 2022 16:00:30 GMT

    {"message":"There no user with id: 100"}


## Login into system

### Request

`POST /api/login`

All test users: Alexey, Petr, Ivan have same password: "pass123".
The user Nik you created has a password: "nikpass".

    for unix bash:
    curl -i -X POST http://localhost:8181/api/login -H "Content-Type:application/json" -d '{"name":"Petr", "password":"pass123"}'
    
    for windows cmd:
    curl -i -X POST http://localhost:8181/api/login -H "Content-Type:application/json" -d "{\"name\": \"Petr\",  \"password\" : \"pass123\" }" 

### Response

    HTTP/1.1 200
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Thu, 21 Apr 2022 16:23:37 GMT

    {"token":"<your token value>"}


## Create a new message

### Request

`POST /api/message`

Here you can copy/paste token value from previous action, or just use this line. For test target I create one infinity token. It`s owner is Petr.

    for unix bash:
    curl -i -X POST http://localhost:8181/api/message -H "Content-Type:application/json" -H "Authorization: Bearer_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxMDk2NTA0NzE2MzZ9.xInGlbdxbIAc2Lii9bkAD_erkxEV-tDjfoGAP3App5Y" -d '{"name":"Petr", "message":"My name is Petr! Hello!"}'
    
    for windows cmd:
    curl -i -X POST http://localhost:8181/api/message -H "Content-Type:application/json" -H "Authorization: Bearer_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxMDk2NTA0NzE2MzZ9.xInGlbdxbIAc2Lii9bkAD_erkxEV-tDjfoGAP3App5Y" -d "{\"name\": \"Petr\",  \"message\" : \"My name is Petr! Hello!\" }"

### Response

    HTTP/1.1 200
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Thu, 21 Apr 2022 16:44:48 GMT

    {"id":40,"dateTimeOfMessage":[2022,4,21,16,44,48,34852000],"value":"My name is Petr! Hello!","authorId":2,"authorName":"Petr"}
    
    
## Read history of message

### Request

`GET /api/message`

Here we get 10 last messages, but if you want you can specify another count. If you try specify negative value or another invalid data, you will get 10 messages. It is default value for case when program can not parse string to integer.

    for unix bash:
    curl -i -X GET http://localhost:8181/api/message -H "Content-Type:application/json" -H "Authorization: Bearer_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxMDk2NTA0NzE2MzZ9.xInGlbdxbIAc2Lii9bkAD_erkxEV-tDjfoGAP3App5Y" -d '{"name":"Petr", "message":"history 10"}'
    
    for windows cmd:
    curl -i -X GET http://localhost:8181/api/message -H "Content-Type:application/json" -H "Authorization: Bearer_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxMDk2NTA0NzE2MzZ9.xInGlbdxbIAc2Lii9bkAD_erkxEV-tDjfoGAP3App5Y" -d "{\"name\": \"Petr\",  \"message\" : \"history 10\" }"

### Response

    HTTP/1.1 200
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Thu, 21 Apr 2022 16:53:29 GMT

    [{"id":40,"dateTimeOfMessage":[2022,4,21,16,44,48,34852000],"value":"My name is Petr! Hello!","authorId":2,"authorName":"Petr"},
    {"id":25,"dateTimeOfMessage":[2022,4,1,13,13],"value":"Petr message 13","authorId":2,"authorName":"Petr"},
    {"id":24,"dateTimeOfMessage":[2022,4,1,13,12],"value":"Petr message 12","authorId":2,"authorName":"Petr"},
    {"id":23,"dateTimeOfMessage":[2022,4,1,13,11],"value":"Petr message 11","authorId":2,"authorName":"Petr"},
    {"id":22,"dateTimeOfMessage":[2022,4,1,13,10],"value":"Petr message 10","authorId":2,"authorName":"Petr"},
    {"id":21,"dateTimeOfMessage":[2022,4,1,13,9],"value":"Petr message 9","authorId":2,"authorName":"Petr"},
    {"id":20,"dateTimeOfMessage":[2022,4,1,13,8],"value":"Petr message 8","authorId":2,"authorName":"Petr"},
    {"id":19,"dateTimeOfMessage":[2022,4,1,13,7],"value":"Petr message 7","authorId":2,"authorName":"Petr"},
    {"id":18,"dateTimeOfMessage":[2022,4,1,13,6],"value":"Petr message 6","authorId":2,"authorName":"Petr"},
    {"id":17,"dateTimeOfMessage":[2022,4,1,13,5],"value":"Petr message 5","authorId":2,"authorName":"Petr"}]
    
    
    
### That`s all, thank you for reading