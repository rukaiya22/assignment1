package ie.setu.domain.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UserDAO {
    fun getAll() : ArrayList<User>{
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it)) }
        }
        return userList
    }

    fun findById(id: Int): User?{
        return transaction {
            Users.selectAll().where { Users.id eq id }
                .limit(1)
                .firstOrNull()
                ?.let { it ->
                    mapToUser(it)
                }
        }
    }

    fun save(user: User) : Int?{
        return transaction {
            Users.insert {
                it[name] = user.name
                it[email] = user.email
            } get Users.id
        }
    }

    fun findByEmail(email: String) :User?{
        return transaction {
            Users.selectAll().where { Users.email eq email }
                .limit(1)
                .firstOrNull()
                ?.let { row ->
                    User(
                        id = row[Users.id],
                        name = row[Users.name],
                        email = row[Users.email]
                    )
                }
        }
    }

    fun delete(id: Int) {
        transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }

    fun update(id: Int, user: User){
        transaction {
            Users.update { Users.id eq id }
        }
    }
}