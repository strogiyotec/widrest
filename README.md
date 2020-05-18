# widrest
Plain REST API to play with in memory widgets 

# Description

This project implements REST API to work with in memory widgets.  
All widgets are stored in memory.  
The project has two profiles: `default,db`. Default uses in memory **ConcurrentHashMap** to store widget.
If `db` profiles was chosen , then H2 DB is used .

# Dependencies

- Spring boot 2
- H2
- Lombok
- Spring Jdbc

# Opeations

- Create  
    Widget create example:   
    `curl --header "Content-Type: application/json"   --request POST   --data '{"x":12,"y":2,"z":15,"width":12,"height":12}'   http://localhost:8080/widgets`  
    Example output:  
    `{"lastModified":1589830808026,"x":12,"y":2,"z":15,"width":12,"height":12,"id":1}`
    Status code - 200

- GET  
    Widget get example:  
    ` curl -i  http://localhost:8080/widgets/1`  
    Status codes:  
            200 - found  
            404 - not found
- DELETE  
    Widget delete example
    `curl -i  -X DELETE http://localhost:8080/widgets/1`  
    Status codes:
            204 - deleted
            404 - not found
- GET ALL
    Widgets get example:  
    `curl -i  http://localhost:8080/widgets/`  
    Status codes:
            200 - found
            204 - empty storage
- UPDATE  
    Widget update example:  
    `curl -i --header "Content-Type: application/json"   --request PUT   --data '{"x":50,"y":22,"z":15,"width":12,"height":12}'   http://localhost:8080/widgets/2`  
    Statues codes:  
            200 - updated
            404 - not found

## Complications 

- In memory H2 when profile is **db**  
        (Add **spring.profiles.active=db** to **application.properties**)

- Pagination (default size is 10, max size is 500)


## Test coverage

Project uses [jacoco](https://www.eclemma.org/jacoco/). Test coverage is 86%

![jacoco](https://github.com/strogiyotec/widrest/blob/master/img/jacoco.png)


