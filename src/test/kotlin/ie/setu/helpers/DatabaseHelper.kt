package ie.setu.helpers

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseHelper {

    fun setupTestDatabase(db: Database) {
        transaction(db) {
            // Create the `users` table
            exec(
                """
                CREATE TABLE users (
                   id SERIAL PRIMARY KEY,
                   name VARCHAR (100) NOT NULL,
                   email VARCHAR (255) UNIQUE NOT NULL
                );
                """
            )

            // Create the `trackers` table
            exec(
                """
                CREATE TABLE trackers (
                   id SERIAL PRIMARY KEY,
                   user_id INTEGER NOT NULL,
                   calories DOUBLE PRECISION NOT NULL,
                   drinking DOUBLE PRECISION NOT NULL,
                   walk_hours DOUBLE PRECISION NOT NULL,

                   CONSTRAINT fk_tracker_user_id
                   FOREIGN KEY (user_id)
                   REFERENCES users(id)
                   ON DELETE CASCADE
                );
                """
            )

            // Create the `diets` table
            exec(
                """
                CREATE TABLE diets (
                   id SERIAL PRIMARY KEY,
                   user_id INTEGER NOT NULL,
                   carbohydrate DOUBLE PRECISION NOT NULL,
                   protein DOUBLE PRECISION NOT NULL,
                   fat DOUBLE PRECISION NOT NULL,

                   CONSTRAINT fk_diet_user_id
                   FOREIGN KEY (user_id)
                   REFERENCES users(id)
                   ON DELETE CASCADE
                );
                """
            )

            // Create the `exercises` table
            exec(
                """
                CREATE TABLE exercises (
                   id SERIAL PRIMARY KEY,
                   user_id INTEGER NOT NULL,
                   running DOUBLE PRECISION NOT NULL,
                   swimming DOUBLE PRECISION NOT NULL,
                   cycling DOUBLE PRECISION NOT NULL,
                   equipment_based DOUBLE PRECISION NOT NULL,

                   CONSTRAINT fk_exercise_user_id
                   FOREIGN KEY (user_id)
                   REFERENCES users(id)
                   ON DELETE CASCADE
                );
                """
            )

            // Create the `rests` table
            exec(
                """
                CREATE TABLE rests (
                   id SERIAL PRIMARY KEY,
                   user_id INTEGER NOT NULL,
                   sleep DOUBLE PRECISION NOT NULL,
                   power_nap DOUBLE PRECISION NOT NULL,
                   meditation DOUBLE PRECISION NOT NULL,

                   CONSTRAINT fk_rest_user_id
                   FOREIGN KEY (user_id)
                   REFERENCES users(id)
                   ON DELETE CASCADE
                );
                """
            )

            // Create the `trackers` table
            exec(
                """
                CREATE TABLE biometrics (
                   id SERIAL PRIMARY KEY,
                   user_id INTEGER NOT NULL,
                   bp_systolic DOUBLE PRECISION NOT NULL,
                   bp_diastolic DOUBLE PRECISION NOT NULL,
                   blood_sugar DOUBLE PRECISION NOT NULL,
                   cholesterol DOUBLE PRECISION NOT NULL,

                   CONSTRAINT fk_biometric_user_id
                   FOREIGN KEY (user_id)
                   REFERENCES users(id)
                   ON DELETE CASCADE
                );
                """
            )

            // Create the `suppliments` table
            exec(
                """
                CREATE TABLE suppliments (
                   id SERIAL PRIMARY KEY,
                   user_id INTEGER NOT NULL,
                   vitamin_d DOUBLE PRECISION NOT NULL,
                   vitamin_c DOUBLE PRECISION NOT NULL,
                   iron DOUBLE PRECISION NOT NULL,
                   calcium DOUBLE PRECISION NOT NULL,

                   CONSTRAINT fk_suppliment_user_id
                   FOREIGN KEY (user_id)
                   REFERENCES users(id)
                   ON DELETE CASCADE
                );
                """
            )

            // Create the `sports` table
            exec(
                """
                CREATE TABLE sports (
                   id SERIAL PRIMARY KEY,
                   user_id INTEGER NOT NULL,
                   sports_name DOUBLE PRECISION NOT NULL,
                   playing_hours DOUBLE PRECISION NOT NULL,

                   CONSTRAINT fk_sport_user_id
                   FOREIGN KEY (user_id)
                   REFERENCES users(id)
                   ON DELETE CASCADE
                );
                """
            )

            // Insert test data into `users` table
            exec(
                """
                INSERT INTO users VALUES
                (1, 'Homer Simpson', 'homer@simpson.com'),
                (2, 'Marge Simpson', 'marge@simpson.com');
                """
            )

            // Insert test data into `trackers` table
            exec(
                """
                INSERT INTO trackers VALUES
                (1, 1, 100, 5, 2),
                (2, 1, 120, 6, 0.5),
                (3, 1, 50, 2, 8),
                (4, 2, 200, 0.5, 0.2),
                (5, 2, 500, 1.5, 0.1),
                (6, 2, 300, 3, 0.8),
                (7, 2, 350, 2, 0.5);
                """
            )

            // Insert test data into `diets` table
            exec(
                """
                INSERT INTO diets VALUES
                (1, 1, 100, 5, 2),
                (2, 1, 120, 6, 0.5),
                (3, 1, 50, 2, 8),
                (4, 2, 200, 0.5, 0.2),
                (5, 2, 500, 1.5, 0.1),
                (6, 2, 300, 3, 0.8),
                (7, 2, 350, 2, 0.5);
                """
            )

            // Insert test data into `exercises` table
            exec(
                """
                INSERT INTO exercises VALUES
                (1, 1, 100, 5, 2, 5),
                (2, 1, 120, 6, 0.5, 7),
                (3, 1, 50, 2, 8, 9),
                (4, 2, 200, 0.5, 0.2, 11),
                (5, 2, 500, 1.5, 0.1, 13),
                (6, 2, 300, 3, 0.8, 15),
                (7, 2, 350, 2, 0.5, 17);
                """
            )

            // Insert test data into `rests` table
            exec(
                """
                INSERT INTO rests VALUES
                (1, 1, 10, 0.5, 2),
                (2, 1, 12, 0.6, 0.5),
                (3, 1, 5, 0.2, 8),
                (4, 2, 2, 0.5, 0.2),
                (5, 2, 5, 1.5, 0.1),
                (6, 2, 3, 0.3, 0.8),
                (7, 2, 3, 0.2, 0.5);
                """
            )

            // Insert test data into `biometrics` table
            exec(
                """
                INSERT INTO biometrics VALUES
                (1, 1, 100, 85, 6.2, 5),
                (2, 1, 120, 66, 7.5, 7),
                (3, 1, 50, 92, 9.8, 9),
                (4, 2, 200, 195, 8.2, 8.1),
                (5, 2, 100, 75, 7.1, 9.3),
                (6, 2, 100, 73, 12.8, 6.5),
                (7, 2, 150, 82, 6.5, 6.7);
                """
            )

            // Insert test data into `suppliments` table
            exec(
                """
                INSERT INTO suppliments VALUES
                (1, 1, 100, 85, 6.2, 5),
                (2, 1, 120, 66, 7.5, 7),
                (3, 1, 50, 92, 9.8, 9),
                (4, 2, 200, 195, 8.2, 8.1),
                (5, 2, 100, 75, 7.1, 9.3),
                (6, 2, 100, 73, 12.8, 6.5),
                (7, 2, 150, 82, 6.5, 6.7);
                """
            )

            // Insert test data into `sports` table
            exec(
                """
                INSERT INTO sports VALUES
                (1, 1, "cricket", 2),
                (2, 1, "football", 0.5),
                (3, 1, "tennis", 3),
                (4, 2, "cricket", 0.2),
                (5, 2, "football", 0.1),
                (6, 2, "hurling", 0.8),
                (7, 2, "tennis", 0.5);
                """
            )

        }
    }

    fun cleanupTestDatabase(db: Database) {
        transaction(db) {
            exec("DROP TABLE IF EXISTS users;")
            exec("DROP TABLE IF EXISTS trackers;")
            exec("DROP TABLE IF EXISTS diets;")
            exec("DROP TABLE IF EXISTS exercises;")
            exec("DROP TABLE IF EXISTS rests;")
            exec("DROP TABLE IF EXISTS swimming;")
            exec("DROP TABLE IF EXISTS suppliments;")
            exec("DROP TABLE IF EXISTS sports;")
        }
    }
}

