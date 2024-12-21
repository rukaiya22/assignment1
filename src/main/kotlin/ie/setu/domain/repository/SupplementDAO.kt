package ie.setu.domain.repository

import ie.setu.domain.Supplement
import ie.setu.domain.db.Supplements
import ie.setu.utils.mapToSupplement
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class SupplementDAO {
    fun getAll() : ArrayList<Supplement>{
        val supplementList: ArrayList<Supplement> = arrayListOf()
        transaction {
            Supplements.selectAll().map {
                supplementList.add(mapToSupplement(it)) }
        }
        return supplementList
    }

    fun save(supplement: Supplement) {
        transaction {
            Supplements.insert {
                it[userId] = supplement.userId
                it[vitamin_d] = supplement.vitamin_d
                it[vitamin_c] = supplement.vitamin_c
                it[iron] = supplement.iron
                it[calcium] = supplement.calcium
            }
        }
    }

    fun update(id: Int, supplement: Supplement){
        transaction {
            Supplements.update({ Supplements.id eq id }) {
                it[vitamin_d] = supplement.vitamin_d
                it[vitamin_c] = supplement.vitamin_c
                it[iron] = supplement.iron
                it[calcium] = supplement.calcium
            }
        }
    }

    fun delete(id: Int) {
        transaction {
            Supplements.deleteWhere { Supplements.id eq id }
        }
    }

    fun findByUserId(userId: Int): ArrayList<Supplement>{
        val supplementList: ArrayList<Supplement> = arrayListOf()
        transaction {
            Supplements.selectAll().where { Supplements.userId eq userId }.map {
                supplementList.add(mapToSupplement(it)) }
        }
        return supplementList
    }

}