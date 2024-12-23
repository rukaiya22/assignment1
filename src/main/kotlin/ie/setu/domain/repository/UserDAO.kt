package ie.setu.domain.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Manages the database transactions and returns the results of the transactions
 */

class UserDAO {
    fun getAll() : ArrayList<User>{
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it)) }
        }
        return userList
    }

    /**
     * Finds a user with [id] from the Users table.
     * @return the user having the id.
     */
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
    /**
     * Adds a [user] to the Users table.
     * @return the id of the user following the add.
     */
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

    /**
     * Delete a user with [id] from the Users table.
     * @return nothing.
     */
    fun delete(id: Int) {
        transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }

    fun update(id: Int, user: User){
        transaction {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[email] = user.email
            }
        }
    }
}