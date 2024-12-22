package ie.setu.domain

import java.time.LocalDate

data class Appointment (
    val id: Int = 0,
    val userId:Int = 0,
    val appointment_type:String = "",
    val appointment_date: LocalDate,
)