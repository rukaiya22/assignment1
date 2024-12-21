package ie.setu.repository

import ie.setu.domain.Exercise
import ie.setu.domain.User
import ie.setu.domain.db.Exercises
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ExerciseDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.exercises
import ie.setu.helpers.users
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class ExerciseDAOTest {

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
        fun `getting all exercises from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, exerciseDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, exerciseDao.getAll().size)
            }
        }

        @Test
        fun `get exercises by a user id that doesn't exist, results in no exercises returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, exerciseDao) = populateUserTable()

                //Act & Assert
                assertEquals(0, exerciseDao.findByUserId(5).size)
            }
        }

        @Test
        fun `get exercises by a user id that exists, results all exercises returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, exerciseDao) = populateUserTable()
                //Act & Assert
                assertEquals(4, exerciseDao.findByUserId(1).size)
            }

        }


    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple exercise added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, exerciseDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, exerciseDao.getAll().size)
                exerciseDao.findByUserId(1).forEach({
                        it -> assertEquals(1, it.userId)
                })

            }
        }
    }

    @Nested
    inner class UpdateExercises {
        @Test
        fun `updating a existant user in table results`() {
            transaction {
                val (_, exerciseDao) = populateUserTable()
                //Act & Assert
                val exercise = exerciseDao.findByUserId(1)[0]
                assertEquals(exercise.running, 100.0)
                val id = exercise.id
                val newExercise = Exercise(userId = 1, running = 150.0, swimming = 5.3, cycling = 3.5, equipment_based = 3.5, id = id)
                exerciseDao.update(id, newExercise)
                val updateExercise = exerciseDao.findByUserId(1)[0]
                assertEquals(updateExercise.running, 150.0)
            }
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existant exercise in table results in no deletion`() {
            transaction {
                val (_, exerciseDao) = populateUserTable()
                //Act & Assert
                assertEquals(10, exerciseDao.getAll().size)
                exerciseDao.delete(11)
                assertEquals(10, exerciseDao.getAll().size)
            }
        }

    }


    internal fun populateUserTable(): Pair<UserDAO, ExerciseDAO>{
        SchemaUtils.create(Users)
        SchemaUtils.create(Exercises)
        val userDAO = UserDAO()
        val exerciseDAO = ExerciseDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        exercises.forEach({
                it -> exerciseDAO.save(it)
        })
        return Pair(userDAO, exerciseDAO)
    }
}