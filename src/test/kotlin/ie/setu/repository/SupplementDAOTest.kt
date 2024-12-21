package ie.setu.repository

import ie.setu.domain.Supplement
import ie.setu.domain.db.Supplements
import ie.setu.domain.db.Users
import ie.setu.domain.repository.SupplementDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.supplements
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class SupplementDAOTest {

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
        fun `getting all supplements from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, supplementDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, supplementDao.getAll().size)
            }
        }

        @Test
        fun `get supplements by a user id that doesn't exist, results in no supplements returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, supplementDao) = populateUserTable()

                //Act & Assert
                assertEquals(0, supplementDao.findByUserId(5).size)
            }
        }

        @Test
        fun `get supplements by a user id that exists, results all supplements returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, supplementDao) = populateUserTable()
                //Act & Assert
                assertEquals(4, supplementDao.findByUserId(1).size)
            }

        }


    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple supplement added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, supplementDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, supplementDao.getAll().size)
                supplementDao.findByUserId(1).forEach({
                        it -> assertEquals(1, it.userId)
                })

            }
        }
    }

    @Nested
    inner class UpdateSupplements {
        @Test
        fun `updating a existant user in table results`() {
            transaction {
                val (_, supplementDao) = populateUserTable()
                //Act & Assert
                val supplement = supplementDao.findByUserId(1)[0]
                assertEquals(supplement.vitamin_d, 100.0)
                val id = supplement.id
                val newSupplement = Supplement(userId = 1, vitamin_d = 150.0, vitamin_c = 5.3, iron = 3.5, calcium = 3.5, id = id)
                supplementDao.update(id, newSupplement)
                val updateSupplement = supplementDao.findByUserId(1)[0]
                assertEquals(updateSupplement.vitamin_d, 150.0)
            }
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existant supplement in table results in no deletion`() {
            transaction {
                val (_, supplementDao) = populateUserTable()
                //Act & Assert
                assertEquals(10, supplementDao.getAll().size)
                supplementDao.delete(11)
                assertEquals(10, supplementDao.getAll().size)
            }
        }

    }


    internal fun populateUserTable(): Pair<UserDAO, SupplementDAO>{
        SchemaUtils.create(Users)
        SchemaUtils.create(Supplements)
        val userDAO = UserDAO()
        val supplementDAO = SupplementDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        supplements.forEach({
                it -> supplementDAO.save(it)
        })
        return Pair(userDAO, supplementDAO)
    }
}