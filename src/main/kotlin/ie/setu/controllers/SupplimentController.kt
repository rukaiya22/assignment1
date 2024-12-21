package ie.setu.controllers

import ie.setu.domain.Suppliment
import ie.setu.domain.repository.SupplimentDAO



class SupplimentController {
    private val supplimentDAO = SupplimentDAO()

    fun getAllSuppliments(): ArrayList<Suppliment> {
        return supplimentDAO.getAll()
    }

    fun getSupplimentsByUserId(userId: Int): ArrayList<Suppliment> {
        return supplimentDAO.findByUserId(userId)
    }

    fun createSuppliment(suppliment: Suppliment): Int {
        supplimentDAO.save(suppliment)
        return 201
    }

    fun updateSuppliment(suppliment: Suppliment): Int {
        supplimentDAO.update(suppliment.id, suppliment)
        return 201
    }

    fun deletedSuppliment(supplimentId: Int): Int {
        supplimentDAO.delete(supplimentId)
        return 204
    }

}