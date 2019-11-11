# doobie-tapir-example

## Populate the database
This has been taken from the Doobie introduction. I used postgres.app for macOs.
```
$ curl -O https://raw.githubusercontent.com/tpolecat/doobie/series/0.7.x/world.sql
$ psql -c 'create user postgres createdb'
$ psql -c 'create database world;' -U postgres
```
At this point flyway should take over when you run the app so the following should no longer be valid.
```
$ psql -c '\i world.sql' -d world -U postgres
$ psql -d world -c "create type myenum as enum ('foo', 'bar')" -U postgres
$ psql -d world -c "create extension postgis" -U postgres
```

## Run
Just run the Server.scala in Intellij.

## Usage
Once running click on the link below to the Swagger docs where you can run and test your the endpoints. 
http://localhost:8080/docs
