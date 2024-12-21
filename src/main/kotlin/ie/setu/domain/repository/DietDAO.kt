package ie.setu.domain.repository

import ie.setu.domain.Diet
import ie.setu.domain.db.Diets
import ie.setu.utils.mapToDiet
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class DietDAO {
    fun getAll() : ArrayList<Diet>{
        val dietList: ArrayList<Diet> = arrayListOf()
        transaction {
            Diets.selectAll().map {
                dietList.add(mapToDiet(it)) }
        }
        return dietList
    }

    fun save(diet: Diet) {
        transaction {
            Diets.insert {
                it[userId] = diet.userId
                it[carbohydrate] = diet.carbohydrate
                it[protein] = diet.protein
                it[fat] = diet.fat
            }
        }
    }

    fun update(id: Int, diet: Diet){
        transaction {
            Diets.update({ Diets.id eq id }) {
                it[carbohydrate] = diet.carbohydrate
                it[protein] = diet.protein
                it[fat] = diet.fat
            }
        }
    }

    fun delete(id: Int) {
        transaction {
            Diets.deleteWhere { Diets.id eq id }
        }
    }

    fun findByUserId(userId: Int): ArrayList<Diet>{
        val dietList: ArrayList<Diet> = arrayListOf()
        transaction {
            Diets.selectAll().where { Diets.userId eq userId }.map {
                dietList.add(mapToDiet(it)) }
        }
        return dietList
    }

}