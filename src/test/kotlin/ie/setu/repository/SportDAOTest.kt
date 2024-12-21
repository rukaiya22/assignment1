package ie.setu.repository

import ie.setu.domain.Sport
import ie.setu.domain.User
import ie.setu.domain.db.Sports
import ie.setu.domain.db.Users
import ie.setu.domain.repository.SportDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.sports
import ie.setu.helpers.users
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class SportDAOTest {

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
        fun `getting all sports from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, sportDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, sportDao.getAll().size)
            }
        }

        @Test
        fun `get sports by a user id that doesn't exist, results in no sports returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, sportDao) = populateUserTable()

                //Act & Assert
                assertEquals(0, sportDao.findByUserId(5).size)
            }
        }

        @Test
        fun `get sports by a user id that exists, results all sports returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, sportDao) = populateUserTable()
                //Act & Assert
                assertEquals(4, sportDao.findByUserId(1).size)
            }

        }


    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple sport added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, sportDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, sportDao.getAll().size)
                sportDao.findByUserId(1).forEach({
                        it -> assertEquals(1, it.userId)
                })

            }
        }
    }

    @Nested
    inner class UpdateSports {
        @Test
        fun `updating a existant user in table results`() {
            transaction {
                val (_, sportDao) = populateUserTable()
                //Act & Assert
                val sport = sportDao.findByUserId(1)[0]
                assertEquals(sport.sports_name, "cricket")
                val id = sport.id
                val newSport = Sport(userId = 1, sports_name = "tennis", playing_hours = 5.3, id = id)
                sportDao.update(id, newSport)
                val updateSport = sportDao.findByUserId(1)[0]
                assertEquals(updateSport.sports_name, "tennis")
            }
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existant sport in table results in no deletion`() {
            transaction {
                val (_, sportDao) = populateUserTable()
                //Act & Assert
                assertEquals(10, sportDao.getAll().size)
                sportDao.delete(11)
                assertEquals(10, sportDao.getAll().size)
            }
        }

    }


    internal fun populateUserTable(): Pair<UserDAO, SportDAO>{
        SchemaUtils.create(Users)
        SchemaUtils.create(Sports)
        val userDAO = UserDAO()
        val sportDAO = SportDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        sports.forEach({
                it -> sportDAO.save(it)
        })
        return Pair(userDAO, sportDAO)
    }
}