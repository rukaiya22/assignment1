package ie.setu.domain.repository

import ie.setu.domain.Exercise
import ie.setu.domain.db.Exercises
import ie.setu.utils.mapToExercise
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ExerciseDAO {
    fun getAll() : ArrayList<Exercise>{
        val exerciseList: ArrayList<Exercise> = arrayListOf()
        transaction {
            Exercises.selectAll().map {
                exerciseList.add(mapToExercise(it)) }
        }
        return exerciseList
    }

    /**
     * Adds a [exercise] to the Exercises table.
     * @return the id of the exercise following the add.
     */
    fun save(exercise: Exercise) {
        transaction {
            Exercises.insert {
                it[userId] = exercise.userId
                it[running] = exercise.running
                it[swimming] = exercise.swimming
                it[cycling] = exercise.cycling
                it[equipment_based] = exercise.equipment_based
            }
        }
    }

    fun update(id: Int, exercise: Exercise){
        transaction {
            Exercises.update({ Exercises.id eq id }) {
                it[running] = exercise.running
                it[swimming] = exercise.swimming
                it[cycling] = exercise.cycling
                it[equipment_based] = exercise.equipment_based
            }
        }
    }

    /**
     * Delete an exercise with [id] from the Exercises table.
     * @return nothing.
     */
    fun delete(id: Int) {
        transaction {
            Exercises.deleteWhere { Exercises.id eq id }
        }
    }

    /**
     * Adds a [userId] to the Exercises table.
     * @return the id of the exercise following the add.
     */
    fun findByUserId(userId: Int): ArrayList<Exercise>{
        val exerciseList: ArrayList<Exercise> = arrayListOf()
        transaction {
            Exercises.selectAll().where { Exercises.userId eq userId }.map {
                exerciseList.add(mapToExercise(it)) }
        }
        return exerciseList
    }

}