Decorator Pattern Workshop
==========================

Initialize the Database
-----------------------
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
mvn package exec:java@main 2> /dev/null
```
