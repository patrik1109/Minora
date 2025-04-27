MinoraTest
Test For Minora Company

Configuration All necessary parameters are located inside the application.properties file:

Path for reading XML file
xml.source.path=src/main/resources/Request.xml

Path for backup of processed XML files
xml.backup.path=src/main/resources/backup

Scheduler delay time in milliseconds (10 minutes)
scheduler.fixedDelay=600000

Available Endpoints

Method | Endpoint | Description POST | http://localhost:8080/events/load | Direct upload of the XML file and save it into the database GET | http://localhost:8080/events/all | Retrieve all uploaded events and products GET | http://localhost:8080/events/products/{insuredId} | Get all products by InsuredId (example: 03003313) GET | http://localhost:8080/h2-console | Access the H2 database console

Important for H2 console: JDBC URL: jdbc:h2:mem:testdb User: sa Password: (leave empty)

Notes XML files are automatically moved to the backup folder after successful processing. A scheduled task will read and save the XML every 10 minutes (configurable).# Minora
