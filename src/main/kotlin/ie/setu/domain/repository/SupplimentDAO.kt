package ie.setu.domain.repository

import ie.setu.domain.Suppliment
import ie.setu.domain.db.Suppliments
import ie.setu.utils.mapToSuppliment
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class SupplimentDAO {
    fun getAll() : ArrayList<Suppliment>{
        val supplimentList: ArrayList<Suppliment> = arrayListOf()
        transaction {
            Suppliments.selectAll().map {
                supplimentList.add(mapToSuppliment(it)) }
        }
        return supplimentList
    }

    fun save(suppliment: Suppliment) {
        transaction {
            Suppliments.insert {
                it[userId] = suppliment.userId
                it[vitamin_d] = suppliment.vitamin_d
                it[vitamin_c] = suppliment.vitamin_c
                it[iron] = suppliment.iron
                it[calcium] = suppliment.calcium
            }
        }
    }

    fun update(id: Int, suppliment: Suppliment){
        transaction {
            Suppliments.update({ Suppliments.id eq id }) {
                it[vitamin_d] = suppliment.vitamin_d
                it[vitamin_c] = suppliment.vitamin_c
                it[iron] = suppliment.iron
                it[calcium] = suppliment.calcium
            }
        }
    }

    fun delete(id: Int) {
        transaction {
            Suppliments.deleteWhere { Suppliments.id eq id }
        }
    }

    fun findByUserId(userId: Int): ArrayList<Suppliment>{
        val supplimentList: ArrayList<Suppliment> = arrayListOf()
        transaction {
            Suppliments.selectAll().where { Suppliments.userId eq userId }.map {
                supplimentList.add(mapToSuppliment(it)) }
        }
        return supplimentList
    }

}