package ie.setu.controllers

import ie.setu.domain.Exercise
import ie.setu.domain.repository.ExerciseDAO



class ExerciseController {
    private val exerciseDAO = ExerciseDAO()

    fun getAllExercises(): ArrayList<Exercise> {
        return exerciseDAO.getAll()
    }

    fun getExercisesByUserId(userId: Int): ArrayList<Exercise> {
        return exerciseDAO.findByUserId(userId)
    }

    fun createExercise(exercise: Exercise): Int {
        exerciseDAO.save(exercise)
        return 201
    }

    fun updateExercise(exercise: Exercise): Int {
        exerciseDAO.update(exercise.id, exercise)
        return 201
    }

    fun deletedExercise(exerciseId: Int): Int {
        exerciseDAO.delete(exerciseId)
        return 204
    }

}