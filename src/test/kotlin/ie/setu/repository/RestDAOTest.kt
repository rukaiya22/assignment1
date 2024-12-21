package ie.setu.repository

import ie.setu.domain.Rest
import ie.setu.domain.User
import ie.setu.domain.db.Rests
import ie.setu.domain.db.Users
import ie.setu.domain.repository.RestDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.rests
import ie.setu.helpers.users
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class RestDAOTest {

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
        fun `getting all rests from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, restDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, restDao.getAll().size)
            }
        }

        @Test
        fun `get rests by a user id that doesn't exist, results in no rests returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, restDao) = populateUserTable()

                //Act & Assert
                assertEquals(0, restDao.findByUserId(5).size)
            }
        }

        @Test
        fun `get rests by a user id that exists, results all rests returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, restDao) = populateUserTable()
                //Act & Assert
                assertEquals(4, restDao.findByUserId(1).size)
            }

        }


    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple rest added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, restDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, restDao.getAll().size)
                restDao.findByUserId(1).forEach({
                        it -> assertEquals(1, it.userId)
                })

            }
        }
    }

    @Nested
    inner class UpdateRests {
        @Test
        fun `updating a existant user in table results`() {
            transaction {
                val (_, restDao) = populateUserTable()
                //Act & Assert
                val rest = restDao.findByUserId(1)[0]
                assertEquals(rest.sleep, 100.0)
                val id = rest.id
                val newRest = Rest(userId = 1, sleep = 150.0, power_nap = 5.3, meditation = 3.5, id = id)
                restDao.update(id, newRest)
                val updateRest = restDao.findByUserId(1)[0]
                assertEquals(updateRest.sleep, 150.0)
            }
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existant rest in table results in no deletion`() {
            transaction {
                val (_, restDao) = populateUserTable()
                //Act & Assert
                assertEquals(10, restDao.getAll().size)
                restDao.delete(11)
                assertEquals(10, restDao.getAll().size)
            }
        }

    }


    internal fun populateUserTable(): Pair<UserDAO, RestDAO>{
        SchemaUtils.create(Users)
        SchemaUtils.create(Rests)
        val userDAO = UserDAO()
        val restDAO = RestDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        rests.forEach({
                it -> restDAO.save(it)
        })
        return Pair(userDAO, restDAO)
    }
}