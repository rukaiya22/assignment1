package ie.setu.controllers

import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO

class UserController {
    private val userDao = UserDAO()

    fun getAllUsers(): ArrayList<User> {
        return userDao.getAll()
    }

    fun getUserById(id: Int): User? {
        return userDao.findById(id)
    }

    fun getUserByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }

    fun createUser(user: User): User? {
        val userId = userDao.save(user)
        if (userId != null)
            return user
        return null
    }

    fun deleteUser(userId: Int): Int {
        userDao.delete(userId)
        return 204
    }
}