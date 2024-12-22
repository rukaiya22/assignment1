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

    fun delete(id: Int) {
        transaction {
            Appointments.deleteWhere { Appointments.id eq id }
        }
    }

    fun findByUserId(userId: Int): ArrayList<Appointment>{
        val appointmentList: ArrayList<Appointment> = arrayListOf()
        transaction {
            Appointments.selectAll().where { Appointments.userId eq userId }.map {
                appointmentList.add(mapToAppointment(it)) }
        }
        return appointmentList
    }

}