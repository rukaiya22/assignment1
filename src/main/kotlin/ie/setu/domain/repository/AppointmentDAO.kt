package ie.setu.domain.repository

import ie.setu.domain.Appointment
import ie.setu.domain.db.Appointments
import ie.setu.utils.mapToAppointment
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class AppointmentDAO {
    fun getAll() : ArrayList<Appointment>{
        val appointmentList: ArrayList<Appointment> = arrayListOf()
        transaction {
            Appointments.selectAll().map {
                appointmentList.add(mapToAppointment(it)) }
        }
        return appointmentList
    }

    /**
     * Adds a [appointment] to the Appointments table.
     * @return the id of the appointment following the add.
     */
    fun save(appointment: Appointment) {
        transaction {
            Appointments.insert {
                it[userId] = appointment.userId
                it[appointment_type] = appointment.appointment_type
                it[appointment_date] = appointment.appointment_date
            }
        }
    }

    fun update(id: Int, appointment: Appointment){
        transaction {
            Appointments.update({ Appointments.id eq id }) {
                it[appointment_type] = appointment.appointment_type
                it[appointment_date] = appointment.appointment_date
            }
        }
    }

    /**
     * Delete a appointment with [id] from the Appointments table.
     * @return nothing.
     */
    fun delete(id: Int) {
        transaction {
            Appointments.deleteWhere { Appointments.id eq id }
        }
    }

    /**
     * Finds an appointments with [userId] from the Appointments table.
     * @return the appointments having the id.
     */
    fun findByUserId(userId: Int): ArrayList<Appointment>{
        val appointmentList: ArrayList<Appointment> = arrayListOf()
        transaction {
            Appointments.selectAll().where { Appointments.userId eq userId }.map {
                appointmentList.add(mapToAppointment(it)) }
        }
        return appointmentList
    }

}