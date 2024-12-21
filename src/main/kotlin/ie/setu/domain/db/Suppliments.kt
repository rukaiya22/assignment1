package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one Track (one health record).
//       Database wise, this is the table object.

object Suppliments : Table("suppliments") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val vitamin_d = double("vitamin_d")
    val vitamin_c = double("vitamin_c")
    val iron = double("iron")
    val calcium = double("calcium")

    override val primaryKey = PrimaryKey(id, name = "PK_Suppliments_ID")
}