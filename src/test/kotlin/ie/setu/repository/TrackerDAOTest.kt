package ie.setu.repository

import ie.setu.domain.User
import ie.setu.domain.db.Trackers
import ie.setu.domain.db.Users
import ie.setu.domain.repository.TrackerDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.trackers
import ie.setu.helpers.users
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class TrackerDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadUsers {
        @Test
        fun `getting all trackers from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, trackerDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, trackerDao.getAll().size)
            }
        }

        @Test
        fun `get trackers by a user id that doesn't exist, results in no trackers returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, trackerDao) = populateUserTable()

                //Act & Assert
                assertEquals(0, trackerDao.findByUserId(5).size)
            }
        }

        @Test
        fun `get trackers by a user id that exists, results all trackers returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, trackerDao) = populateUserTable()
                //Act & Assert
                assertEquals(4, trackerDao.findByUserId(1).size)
            }

        }


    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple users added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, trackerDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, trackerDao.getAll().size)
                trackerDao.findByUserId(1).forEach({
                    it -> assertEquals(user1.id, it.userId)
                })

            }
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existant user in table results in no deletion`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, trackerDao) = populateUserTable()
                //Act & Assert
                assertEquals(10, trackerDao.getAll().size)
                trackerDao.deleteByUserId(1)
                assertEquals(6, trackerDao.getAll().size)
            }
        }

    }


    internal fun populateUserTable(): Pair<UserDAO, TrackerDAO>{
        SchemaUtils.create(Users)
        SchemaUtils.create(Trackers)
        val userDAO = UserDAO()
        val trackerDAO = TrackerDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        trackers.forEach({
            it -> trackerDAO.save(it)
        })
        return Pair(userDAO, trackerDAO)
    }
}