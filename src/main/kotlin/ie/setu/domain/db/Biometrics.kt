package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one Track (one health record).
//       Database wise, this is the table object.

object Biometrics : Table("biometrics") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val bp_systolic = double("bp_systolic")
    val bp_diastolic = double("bp_diastolic")
    val blood_sugar = double("blood_sugar")
    val cholesterol = double("cholesterol")

    override val primaryKey = PrimaryKey(id, name = "PK_Biometrics_ID")
}