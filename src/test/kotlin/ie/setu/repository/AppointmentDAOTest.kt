package ie.setu.repository

import ie.setu.domain.Appointment
import ie.setu.domain.User
import ie.setu.domain.db.Appointments
import ie.setu.domain.db.Users
import ie.setu.domain.repository.AppointmentDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.appointments
import ie.setu.helpers.users
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate


class AppointmentDAOTest {

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
        fun `getting all appointments from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, appointmentDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, appointmentDao.getAll().size)
            }
        }

        @Test
        fun `get appointments by a user id that doesn't exist, results in no appointments returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, appointmentDao) = populateUserTable()

                //Act & Assert
                assertEquals(0, appointmentDao.findByUserId(5).size)
            }
        }

        @Test
        fun `get appointments by a user id that exists, results all appointments returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, appointmentDao) = populateUserTable()
                //Act & Assert
                assertEquals(4, appointmentDao.findByUserId(1).size)
            }

        }


    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple appointment added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val (_, appointmentDao) = populateUserTable()

                //Act & Assert
                assertEquals(10, appointmentDao.getAll().size)
                appointmentDao.findByUserId(1).forEach({
                        it -> assertEquals(1, it.userId)
                })

            }
        }
    }

    @Nested
    inner class UpdateAppointments {
        @Test
        fun `updating a existant user in table results`() {
            transaction {
                val (_, appointmentDao) = populateUserTable()
                //Act & Assert
                val appointment = appointmentDao.findByUserId(1)[0]
                assertEquals(appointment.appointment_type, "nutritionist")
                val id = appointment.id
                val newAppointment = Appointment(userId = 1, appointment_type = "physiotherapist", appointment_date = LocalDate.now(), id = id)
                appointmentDao.update(id, newAppointment)
                val updateAppointment = appointmentDao.findByUserId(1)[0]
                assertEquals(updateAppointment.appointment_type, "physiotherapist")
            }
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existant appointment in table results in no deletion`() {
            transaction {
                val (_, appointmentDao) = populateUserTable()
                //Act & Assert
                assertEquals(10, appointmentDao.getAll().size)
                appointmentDao.delete(11)
                assertEquals(10, appointmentDao.getAll().size)
            }
        }

    }


    internal fun populateUserTable(): Pair<UserDAO, AppointmentDAO>{
        SchemaUtils.create(Users)
        SchemaUtils.create(Appointments)
        val userDAO = UserDAO()
        val appointmentDAO = AppointmentDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        appointments.forEach({
                it -> appointmentDAO.save(it)
        })
        return Pair(userDAO, appointmentDAO)
    }
}