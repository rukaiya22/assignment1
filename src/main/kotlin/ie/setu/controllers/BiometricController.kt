package ie.setu.controllers

import ie.setu.domain.Biometric
import ie.setu.domain.repository.BiometricDAO



class BiometricController {
    private val biometricDAO = BiometricDAO()

    fun getAllBiometrics(): ArrayList<Biometric> {
        return biometricDAO.getAll()
    }

    fun getBiometricsByUserId(userId: Int): ArrayList<Biometric> {
        return biometricDAO.findByUserId(userId)
    }

    fun createBiometric(biometric: Biometric): Int {
        biometricDAO.save(biometric)
        return 201
    }

    fun updateBiometric(biometric: Biometric): Int {
        biometricDAO.update(biometric.id, biometric)
        return 201
    }

    fun deletedBiometric(biometricId: Int): Int {
        biometricDAO.delete(biometricId)
        return 204
    }

}