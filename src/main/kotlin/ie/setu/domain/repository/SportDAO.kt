package ie.setu.domain.repository

import ie.setu.domain.Sport
import ie.setu.domain.db.Sports
import ie.setu.utils.mapToSport
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class SportDAO {
    fun getAll() : ArrayList<Sport>{
        val sportList: ArrayList<Sport> = arrayListOf()
        transaction {
            Sports.selectAll().map {
                sportList.add(mapToSport(it)) }
        }
        return sportList
    }

    fun save(sport: Sport) {
        transaction {
            Sports.insert {
                it[userId] = sport.userId
                it[sports_name] = sport.sports_name
                it[playing_hours] = sport.playing_hours
            }
        }
    }

    fun update(id: Int, sport: Sport){
        transaction {
            Sports.update({ Sports.id eq id }) {
                it[sports_name] = sport.sports_name
                it[playing_hours] = sport.playing_hours
            }
        }
    }

    fun delete(id: Int) {
        transaction {
            Sports.deleteWhere { Sports.id eq id }
        }
    }

    fun findByUserId(userId: Int): ArrayList<Sport>{
        val sportList: ArrayList<Sport> = arrayListOf()
        transaction {
            Sports.selectAll().where { Sports.userId eq userId }.map {
                sportList.add(mapToSport(it)) }
        }
        return sportList
    }

}