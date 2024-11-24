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
                   walk_hours DOUBLE PRECISION NOT NULL
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
        }
    }

    fun cleanupTestDatabase(db: Database) {
        transaction(db) {
            exec("DROP TABLE IF EXISTS trackers;")
            exec("DROP TABLE IF EXISTS users;")
        }
    }
}

