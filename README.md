# scala-example

An example scala project that aims to create a full working stack in one application.

This project mixes together these technologies

* PostGres - Database
* H2 - Test Database
* Scalikejdbc - The ORM to make database calls
* Flyway - Maintains database schema
* Logback - Logging
* Circle - Decoding to JSON and encoding from JSON
* Javelin - Web server that allows mapping requests to classes for each domain object
* Quartz - Schedules and runs backend jobs
* Kafka - Sending messages
* React - UI

The project can be imported into Intellij from the pom.xml
Run docker compose up to bring up the DB
Edit DB to point toward your DB url
Edit Kafka to point toward your kafka server (or comment out kafkaService.run() in application)




