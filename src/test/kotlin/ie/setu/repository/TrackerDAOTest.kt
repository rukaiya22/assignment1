package ie.setu.repository

import ie.setu.domain.Tracker
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
        fun `multiple tracker added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, trackerDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, trackerDao.getAll().size)
                trackerDao.findByUserId(1).forEach({
                    it -> assertEquals(1, it.userId)
                })

            }
        }
    }

    @Nested
    inner class UpdateTrackers {
        @Test
        fun `updating a existant user in table results`() {
            transaction {
                val (_, trackerDao) = populateUserTable()
                //Act & Assert
                val tracker = trackerDao.findByUserId(1)[0]
                assertEquals(tracker.calories, 100.0)
                val id = tracker.id
                val newTracker = Tracker(userId = 1, calories = 150.0, drinking = 5.3, walkHours = 3.5, id = id)
                trackerDao.update(id, newTracker)
                val updateTracker = trackerDao.findByUserId(1)[0]
                assertEquals(updateTracker.calories, 150.0)
            }
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existant tracker in table results in no deletion`() {
            transaction {
                val (_, trackerDao) = populateUserTable()
                //Act & Assert
                assertEquals(10, trackerDao.getAll().size)
                trackerDao.delete(11)
                assertEquals(10, trackerDao.getAll().size)
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