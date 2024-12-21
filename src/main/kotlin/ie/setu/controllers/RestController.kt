package ie.setu.controllers

import ie.setu.domain.Rest
import ie.setu.domain.repository.RestDAO



class RestController {
    private val restDAO = RestDAO()

    fun getAllRests(): ArrayList<Rest> {
        return restDAO.getAll()
    }

    fun getRestsByUserId(userId: Int): ArrayList<Rest> {
        return restDAO.findByUserId(userId)
    }

    fun createRest(rest: Rest): Int {
        restDAO.save(rest)
        return 201
    }

    fun updateRest(rest: Rest): Int {
        restDAO.update(rest.id, rest)
        return 201
    }

    fun deletedRest(restId: Int): Int {
        restDAO.delete(restId)
        return 204
    }

}