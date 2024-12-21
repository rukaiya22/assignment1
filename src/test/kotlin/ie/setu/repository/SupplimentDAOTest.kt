package ie.setu.repository

import ie.setu.domain.Suppliment
import ie.setu.domain.User
import ie.setu.domain.db.Suppliments
import ie.setu.domain.db.Users
import ie.setu.domain.repository.SupplimentDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.suppliments
import ie.setu.helpers.users
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class SupplimentDAOTest {

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
        fun `getting all suppliments from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, supplimentDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, supplimentDao.getAll().size)
            }
        }

        @Test
        fun `get suppliments by a user id that doesn't exist, results in no suppliments returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, supplimentDao) = populateUserTable()

                //Act & Assert
                assertEquals(0, supplimentDao.findByUserId(5).size)
            }
        }

        @Test
        fun `get suppliments by a user id that exists, results all suppliments returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, supplimentDao) = populateUserTable()
                //Act & Assert
                assertEquals(4, supplimentDao.findByUserId(1).size)
            }

        }


    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple suppliment added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, supplimentDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, supplimentDao.getAll().size)
                supplimentDao.findByUserId(1).forEach({
                        it -> assertEquals(1, it.userId)
                })

            }
        }
    }

    @Nested
    inner class UpdateSuppliments {
        @Test
        fun `updating a existant user in table results`() {
            transaction {
                val (_, supplimentDao) = populateUserTable()
                //Act & Assert
                val suppliment = supplimentDao.findByUserId(1)[0]
                assertEquals(suppliment.vitamin_d, 100.0)
                val id = suppliment.id
                val newSuppliment = Suppliment(userId = 1, vitamin_d = 150.0, vitamin_c = 5.3, iron = 3.5, calcium = 3.5, id = id)
                supplimentDao.update(id, newSuppliment)
                val updateSuppliment = supplimentDao.findByUserId(1)[0]
                assertEquals(updateSuppliment.vitamin_d, 150.0)
            }
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existant suppliment in table results in no deletion`() {
            transaction {
                val (_, supplimentDao) = populateUserTable()
                //Act & Assert
                assertEquals(10, supplimentDao.getAll().size)
                supplimentDao.delete(11)
                assertEquals(10, supplimentDao.getAll().size)
            }
        }

    }


    internal fun populateUserTable(): Pair<UserDAO, SupplimentDAO>{
        SchemaUtils.create(Users)
        SchemaUtils.create(Suppliments)
        val userDAO = UserDAO()
        val supplimentDAO = SupplimentDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        suppliments.forEach({
                it -> supplimentDAO.save(it)
        })
        return Pair(userDAO, supplimentDAO)
    }
}