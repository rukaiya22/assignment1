package ie.setu.repository

import ie.setu.domain.Diet
import ie.setu.domain.User
import ie.setu.domain.db.Diets
import ie.setu.domain.db.Users
import ie.setu.domain.repository.DietDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.diets
import ie.setu.helpers.users
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class DietDAOTest {

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
        fun `getting all diets from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, dietDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, dietDao.getAll().size)
            }
        }

        @Test
        fun `get diets by a user id that doesn't exist, results in no diets returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, dietDao) = populateUserTable()

                //Act & Assert
                assertEquals(0, dietDao.findByUserId(5).size)
            }
        }

        @Test
        fun `get diets by a user id that exists, results all diets returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, dietDao) = populateUserTable()
                //Act & Assert
                assertEquals(4, dietDao.findByUserId(1).size)
            }

        }


    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple diet added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, dietDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, dietDao.getAll().size)
                dietDao.findByUserId(1).forEach({
                        it -> assertEquals(1, it.userId)
                })

            }
        }
    }

    @Nested
    inner class UpdateDiets {
        @Test
        fun `updating a existant user in table results`() {
            transaction {
                val (_, dietDao) = populateUserTable()
                //Act & Assert
                val diet = dietDao.findByUserId(1)[0]
                assertEquals(diet.carbohydrate, 100.0)
                val id = diet.id
                val newDiet = Diet(userId = 1, carbohydrate = 150.0, protein = 5.3, fat = 3.5, id = id)
                dietDao.update(id, newDiet)
                val updateDiet = dietDao.findByUserId(1)[0]
                assertEquals(updateDiet.carbohydrate, 150.0)
            }
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existant diet in table results in no deletion`() {
            transaction {
                val (_, dietDao) = populateUserTable()
                //Act & Assert
                assertEquals(10, dietDao.getAll().size)
                dietDao.delete(11)
                assertEquals(10, dietDao.getAll().size)
            }
        }

    }


    internal fun populateUserTable(): Pair<UserDAO, DietDAO>{
        SchemaUtils.create(Users)
        SchemaUtils.create(Diets)
        val userDAO = UserDAO()
        val dietDAO = DietDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        diets.forEach({
                it -> dietDAO.save(it)
        })
        return Pair(userDAO, dietDAO)
    }
}