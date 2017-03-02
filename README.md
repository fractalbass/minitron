# Minitron

A simple application to be used with the PHG minitron device.  Minitron is an IoT device that uses a
charlieplex LED display for scrolling messages...  sort of like a jumbotron...  but mini.

The code in this repository is a work in progress and demonstrates the following tools and techniques:

*  The code is a Springboot 1.4 app
*  The code uses a postgress database as a data store and is targeted at running on Heroku.
*  The application uses various forms of testing including unit, integration and functional tests
through the use of spock, and various mocking techniques.

To run the tests for this app issue this gradle command:

./gradlew clean build test

To run the app, use this command:

./gradlew run

The application expect for JDBC parameters to be configured via an environment variable.  For example:

sudo ./gradlew -DJDBC_DATABASE_URL=jdbc:postgresql://localhost/milesporter run
