package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one Track (one health record).
//       Database wise, this is the table object.

object Diets : Table("diets") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val carbohydrate = double("carbohydrate")
    val protein = double("protein")
    val fat = double("fat")

    override val primaryKey = PrimaryKey(id, name = "PK_Diets_ID")
}