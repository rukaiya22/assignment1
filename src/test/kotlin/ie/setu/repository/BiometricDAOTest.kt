package ie.setu.repository

import ie.setu.domain.Biometric
import ie.setu.domain.User
import ie.setu.domain.db.Biometrics
import ie.setu.domain.db.Users
import ie.setu.domain.repository.BiometricDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.biometrics
import ie.setu.helpers.users
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class BiometricDAOTest {

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
        fun `getting all biometrics from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, biometricDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, biometricDao.getAll().size)
            }
        }

        @Test
        fun `get biometrics by a user id that doesn't exist, results in no biometrics returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, biometricDao) = populateUserTable()

                //Act & Assert
                assertEquals(0, biometricDao.findByUserId(5).size)
            }
        }

        @Test
        fun `get biometrics by a user id that exists, results all biometrics returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, biometricDao) = populateUserTable()
                //Act & Assert
                assertEquals(4, biometricDao.findByUserId(1).size)
            }

        }


    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple biometric added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, biometricDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, biometricDao.getAll().size)
                biometricDao.findByUserId(1).forEach({
                        it -> assertEquals(1, it.userId)
                })

            }
        }
    }

    @Nested
    inner class UpdateBiometrics {
        @Test
        fun `updating a existant user in table results`() {
            transaction {
                val (_, biometricDao) = populateUserTable()
                //Act & Assert
                val biometric = biometricDao.findByUserId(1)[0]
                assertEquals(biometric.bp_systolic, 100.0)
                val id = biometric.id
                val newBiometric = Biometric(userId = 1, bp_systolic = 150.0, bp_diastolic = 5.3, blood_sugar = 3.5, cholesterol = 3.5, id = id)
                biometricDao.update(id, newBiometric)
                val updateBiometric = biometricDao.findByUserId(1)[0]
                assertEquals(updateBiometric.bp_systolic, 150.0)
            }
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existant biometric in table results in no deletion`() {
            transaction {
                val (_, biometricDao) = populateUserTable()
                //Act & Assert
                assertEquals(10, biometricDao.getAll().size)
                biometricDao.delete(11)
                assertEquals(10, biometricDao.getAll().size)
            }
        }

    }


    internal fun populateUserTable(): Pair<UserDAO, BiometricDAO>{
        SchemaUtils.create(Users)
        SchemaUtils.create(Biometrics)
        val userDAO = UserDAO()
        val biometricDAO = BiometricDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        biometrics.forEach({
                it -> biometricDAO.save(it)
        })
        return Pair(userDAO, biometricDAO)
    }
}