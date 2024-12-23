package ie.setu.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.postgresql.util.PSQLException
import java.util.*

class DbConfig {

    private val logger = KotlinLogging.logger {}
    private lateinit var dbConfig: Database

    fun getDbConnection(runningTest: Boolean = false): Database {
        val dbUrl: String
        val dbDriver: String
        val dbUser: String
        val dbPassword: String

        if (runningTest) {
            // Configuration for in-memory H2 database
            println("=========== Running in test mode ================")
            dbUrl = "jdbc:h2:mem:test_${UUID.randomUUID()};DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;DATABASE_TO_UPPER=false"
            dbDriver = "org.h2.Driver"
            dbUser = "sa"
            dbPassword = ""
        } else {
            // Configuration for production database
            println("=========== Running in production mode ================")

            val PGHOST = "localhost"
            val PGPORT = "5432"
            val PGUSER = "postgres"
            val PGPASSWORD = "postgres"
            val PGDATABASE = "postgres"

//            val PGHOST = "dpg-ctk2pabqf0us739evs9g-a.frankfurt-postgres.render.com"
//            val PGPORT = "5432"
//            val PGUSER = "rukaiya"
//            val PGPASSWORD = "yODkQtfzazpZVyIeT3pCsLLmrRXJxbzt"
//            val PGDATABASE = "health_tracker_65kl"

            dbUrl = "jdbc:postgresql://$PGHOST:$PGPORT/$PGDATABASE"
            dbDriver = "org.postgresql.Driver"
            dbUser = PGUSER
            dbPassword = PGPASSWORD
        }

        try {
            logger.info { "Starting DB Connection...$dbUrl" }
            dbConfig = Database.connect(
                url = dbUrl,
                driver = dbDriver,
                user = dbUser,
                password = dbPassword
            )
            logger.info { "DB Connected Successfully...${dbConfig.url}" }
        } catch (e: PSQLException) {
            logger.error { "Error in DB Connection: ${e.message}" }
            throw RuntimeException("Failed to connect to the database", e)
        }
        return dbConfig
    }
}