package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

// SRP - Responsibility is to manage one Track (one health record).
//       Database wise, this is the table object.

object Appointments : Table("appointments") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val appointment_type = varchar("appointment_type", 100)
    val appointment_date = date("appointment_date")

    override val primaryKey = PrimaryKey(id, name = "PK_Appointments_ID")
}