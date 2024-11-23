package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one Track (one health record).
//       Database wise, this is the table object.

object Trackers : Table("trackers") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val calories = double("calories")
    val drinking = double("drinking")
    val walkHours = double("walk_hours")

    override val primaryKey = PrimaryKey(id, name = "PK_Trackers_ID")
}