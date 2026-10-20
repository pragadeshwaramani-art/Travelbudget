# Travel Budget and Expense Tracker

A Java Swing desktop application using MySQL and JDBC. It is organized into model, DAO, and UI layers so the code is easier to understand and maintain.

## Features
- Add and view trips
- Add and view expenses
- View the total expenses for each trip
- Use MySQL to store records persistently
- Use prepared statements for database operations

## Requirements
- JDK 17 or later
- MySQL Server and MySQL Workbench
- Maven
- VS Code with the Java Extension Pack

## 1. Create the database
Open MySQL Workbench and run all statements in `database/schema.sql`.

## 2. Configure the connection
Open `src/main/java/com/travelbudget/DBConnection.java` and replace `YOUR_MYSQL_PASSWORD` with your MySQL password. Do not upload your real password to GitHub.

The default username is `root` and the database is `travel_budget`.

## 3. Run in VS Code
Open the folder containing `pom.xml`. Wait for Maven dependencies to load, then run `Main.java`.

Alternatively, in the terminal at the project root:
```bash
mvn clean compile
mvn exec:java
```

## Notes
- Maven downloads MySQL Connector/J automatically; you do not need to copy a JAR into `lib`.
- This is a starter college project. It focuses on trip creation, expense creation, and expense totals. Edit/delete features and advanced reports can be added next.
