package ie.setu.controllers

import ie.setu.domain.Supplement
import ie.setu.domain.repository.SupplementDAO



class SupplementController {
    private val supplementDAO = SupplementDAO()

    fun getAllSupplements(): ArrayList<Supplement> {
        return supplementDAO.getAll()
    }

    fun getSupplementsByUserId(userId: Int): ArrayList<Supplement> {
        return supplementDAO.findByUserId(userId)
    }

    fun createSupplement(supplement: Supplement): Int {
        supplementDAO.save(supplement)
        return 201
    }

    fun updateSupplement(supplement: Supplement): Int {
        supplementDAO.update(supplement.id, supplement)
        return 201
    }

    fun deletedSupplement(supplementId: Int): Int {
        supplementDAO.delete(supplementId)
        return 204
    }

}