package ie.setu.controllers

import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO

class UserController {
    private val userDao = UserDAO()

    fun getAllUsers(): ArrayList<User> {
        return userDao.getAll()
    }

    fun createUser(user: User): Int {
        userDao.save(user)
        return 201
    }

    fun deleteUser(userId: Int): Int {
        userDao.delete(userId)
        return 204
    }
}