# Health Tracker App

## Overview
This is a **Health Tracker App** that allows users to manage their health records. It includes functionalities to:
- Add and delete users.
- View all users.
- Manage health records (add, view, and delete health records for users).
- Automatically delete health records when a user is deleted.

## Features
- **User Management**: Add, delete, and view user details.
- **Health Record Management**: Add, view, and delete health records for specific users.
- **Cascade Deletion**: Deleting a user automatically removes all associated health records.
- **API Documentation**: API specifications available at the `/doc` path.

## Getting Started

### Build the Project
Use Maven to build the project:  
```mvn clean install```
### Running the Project
1. **Set Up the Database**:
    - Create a PostgreSQL database.
    - Update the database connection parameters in the `DBConfig.kt` file, located in the `ie.setu.config` package.

2. **Run the Application**:  
   Once the database is configured, run the project using your preferred IDE or Maven.

### Running Tests
All tests use an H2 in-memory database for faster execution and isolation. To run the tests:  

### Running the Project
1. **Set Up the Database**:
    - Create a PostgreSQL database.
    - Update the database connection parameters in the `DBConfig.kt` file, located in the `ie.setu.config` package.

2. **Run the Application**:  
   Once the database is configured, run the project using your preferred IDE or Maven.

### Running Tests
All tests use an H2 in-memory database for faster execution and isolation. To run the tests:  

## API Documentation
The API documentation is available at the `/doc` path after starting the application. It contains detailed specifications of the APIs used in the application.

## Tools & Technologies
- **Programming Language at Backend**: Kotlin
- **Database**: PostgreSQL (production), H2 (testing)
- **Build Tool**: Maven
- **Backend Web**: Javalin
- **Front end Application**: Vue.js

## Author
This project is developed by **Rukaiya Rafique** as part of an assignment for the **Agile course** at **SETU, Waterford**.  