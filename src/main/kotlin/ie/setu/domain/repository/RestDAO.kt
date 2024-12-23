package ie.setu.domain.repository

import ie.setu.domain.Rest
import ie.setu.domain.db.Rests
import ie.setu.utils.mapToRest
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class RestDAO {
    fun getAll() : ArrayList<Rest>{
        val restList: ArrayList<Rest> = arrayListOf()
        transaction {
            Rests.selectAll().map {
                restList.add(mapToRest(it)) }
        }
        return restList
    }

    /**
     * Adds a [rest] to the Rests table.
     * @return the id of the rest following the add.
     */
    fun save(rest: Rest) {
        transaction {
            Rests.insert {
                it[userId] = rest.userId
                it[sleep] = rest.sleep
                it[power_nap] = rest.power_nap
                it[meditation] = rest.meditation
            }
        }
    }

    fun update(id: Int, rest: Rest){
        transaction {
            Rests.update({ Rests.id eq id }) {
                it[sleep] = rest.sleep
                it[power_nap] = rest.power_nap
                it[meditation] = rest.meditation
            }
        }
    }

    /**
     * Delete a rest with [id] from the Rests table.
     * @return nothing.
     */
    fun delete(id: Int) {
        transaction {
            Rests.deleteWhere { Rests.id eq id }
        }
    }

    /**
     * Finds a rests with [userId] from the Rests table.
     * @return the rests having the id.
     */
    fun findByUserId(userId: Int): ArrayList<Rest>{
        val restList: ArrayList<Rest> = arrayListOf()
        transaction {
            Rests.selectAll().where { Rests.userId eq userId }.map {
                restList.add(mapToRest(it)) }
        }
        return restList
    }

}