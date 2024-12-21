package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one Track (one health record).
//       Database wise, this is the table object.

object Sports : Table("sports") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val sports_name = varchar("sports_name", 100)
    val playing_hours = double("playing_hours")

    override val primaryKey = PrimaryKey(id, name = "PK_Sports_ID")
}