Decorator Pattern Workshop
==========================
This application uses the classic computer science example of a bookstore.  It is implemented as a console based application.  It's a work in progress, but it's already apparent that something needs cleaning.  The `BookService` that is the core of the application has two responsibilities: interact with the database and manage caching of those values.  This leads to both a complicated implementation and complicated tests.

Your Mission
------------
1) Refactor the implmentation to split the database and cache responsiblities into two classes and compose them using the decorator pattern.  (Decorate the database class with the cache class)
2) Add a profiler decorator to the `BookService` that logs how long service calls take to complete.
3) Compose your decorators so that both the database class and cache class have profiler decorators.  If you did part 1 and 2 correctly you should only have to change the `BookServiceFactory` class.

Dependencies
------------
The bookstore was written using Java 8 (but should be compatible with Java 1.6 and up).  Maven is used as the build tool.  It was designed to be runnable on the commandline, so it has no dependencies on any particular IDE.  You are free to choose your favorite IDE or hack in the terminal.

The project depends on the following projects.  You shouldn't have to do anything to install them.  Maven will take care of everything.
* [Sqlite](https://www.sqlite.org/)
* [Java Cacheing System (JCS)](https://commons.apache.org/proper/commons-jcs/)
* [Junit](http://junit.org/junit4/)
* [Mockito](http://site.mockito.org/)

Initialize the Application
--------------------------
Before you can do anything, you will need to run this script to create an Sqlite database with some sample data and second Sqlite database that will be used for testing.

```bash
mvn compile exec:java@init
```

Run the tests
-------------
```bash
mvn test
```

Run the bookstore
-----------------
```bash
mvn package exec:java@main
```

Disclaimer
----------
This project does not necessarily represent the best way to interact with a database, cache, or logger in Java.  It is intended to be a simple example where the decorator pattern can be applied.
