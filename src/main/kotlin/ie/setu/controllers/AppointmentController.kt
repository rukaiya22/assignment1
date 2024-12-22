package ie.setu.controllers

import ie.setu.domain.Appointment
import ie.setu.domain.repository.AppointmentDAO



class AppointmentController {
    private val appointmentDAO = AppointmentDAO()

    fun getAllAppointments(): ArrayList<Appointment> {
        return appointmentDAO.getAll()
    }

    fun getAppointmentsByUserId(userId: Int): ArrayList<Appointment> {
        return appointmentDAO.findByUserId(userId)
    }

    fun createAppointment(appointment: Appointment): Int {
        appointmentDAO.save(appointment)
        return 201
    }

    fun updateAppointment(appointment: Appointment): Int {
        appointmentDAO.update(appointment.id, appointment)
        return 201
    }

    fun deletedAppointment(appointmentId: Int): Int {
        appointmentDAO.delete(appointmentId)
        return 204
    }

}