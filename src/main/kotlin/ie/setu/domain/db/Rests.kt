package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one Track (one health record).
//       Database wise, this is the table object.

object Rests : Table("rests") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val sleep = double("sleep")
    val power_nap = double("power_nap")
    val meditation = double("meditation")

    override val primaryKey = PrimaryKey(id, name = "PK_Rests_ID")
}