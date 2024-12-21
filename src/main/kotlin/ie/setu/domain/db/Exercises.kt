package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one Track (one health record).
//       Database wise, this is the table object.

object Exercises : Table("exercises") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val running = double("running")
    val swimming = double("swimming")
    val cycling = double("cycling")
    val equipment_based = double("equipment_based")

    override val primaryKey = PrimaryKey(id, name = "PK_Exercises_ID")
}