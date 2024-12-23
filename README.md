# Health Tracker App

## Overview
This is a **Health Tracker App** that allows users to manage their health records. It includes functionalities to:
- Add and delete users.
- View all users.
- Manage health records (add, view, and delete health records for users).
- Automatically delete health records when a user is deleted.

## Basic Features
- **User Management**: Add, delete, and view user details.
- **Health Record Management**: Add, view, and delete health records for specific users.
- **Cascade Deletion**: Deleting a user automatically removes all associated health records.
- **API Documentation**: API specifications available at the `/doc` path.

## Details of the other features

### Basic Info
- **calories**: Track daily calorie consumption.
- **drinking**: Monitor water or beverage intake.
- **walkingHours**: Log the number of hours spent walking each day.
- 
### Diets
- **carbohydrate**: Track daily carbohydrate intake.
- **protein**: Monitor protein consumption.
- **fat**: Record fat intake to manage dietary balance.

### Exercises
- **running**: Log running sessions and durations.
- **swimming**: Record swimming activities and metrics.
- **cycling**: Track cycling exercises.
- **equipment_based**: Document workouts using gym or fitness equipment.

### Rests
- **sleep**: Monitor sleep duration and quality.
- **power_nap**: Record quick nap sessions for better productivity.
- **meditation**: Track meditation practices and durations.

### Biometrics
- **bp_systolic**: Log systolic blood pressure readings.
- **bp_diastolic**: Record diastolic blood pressure levels.
- **blood_sugar**: Monitor blood sugar levels for health management.
- **cholesterol**: Track cholesterol levels over time.

### Supplements
- **vitamin_d**: Record Vitamin D intake or supplements.
- **vitamin_c**: Track Vitamin C usage.
- **iron**: Log iron supplement consumption.
- **calcium**: Document calcium intake.

### Sports
- **sports_name**: Track the name of the sport being played.
- **playing_hours**: Record the number of hours spent playing sports.

### Appointments
- **appointment_type**: Log the type of appointment (e.g., doctor, therapist, etc.).
- **appointment_date**: Record the date of the appointment.

## How to Use
1. **Add Records**: Use the application's interface to add new entries for any feature.
2. **Update Records**: Modify existing records to reflect changes or updates.
3. **Delete Records**: Remove entries that are no longer relevant.

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
