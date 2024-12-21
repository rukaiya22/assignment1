package ie.setu.controllers

import ie.setu.domain.Diet
import ie.setu.domain.repository.DietDAO



class DietController {
    private val dietDAO = DietDAO()

    fun getAllDiets(): ArrayList<Diet> {
        return dietDAO.getAll()
    }

    fun getDietsByUserId(userId: Int): ArrayList<Diet> {
        return dietDAO.findByUserId(userId)
    }

    fun createDiet(diet: Diet): Int {
        dietDAO.save(diet)
        return 201
    }

    fun updateDiet(diet: Diet): Int {
        dietDAO.update(diet.id, diet)
        return 201
    }

    fun deletedDiet(dietId: Int): Int {
        dietDAO.delete(dietId)
        return 204
    }

}