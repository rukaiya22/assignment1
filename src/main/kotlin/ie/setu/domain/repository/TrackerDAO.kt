package ie.setu.domain.repository

import ie.setu.domain.Tracker
import ie.setu.domain.User
import ie.setu.domain.db.Trackers
import ie.setu.domain.db.Users
import ie.setu.utils.mapToTracker
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class TrackerDAO {
    fun getAll() : ArrayList<Tracker>{
        val trackerList: ArrayList<Tracker> = arrayListOf()
        transaction {
            Trackers.selectAll().map {
                trackerList.add(mapToTracker(it)) }
        }
        return trackerList
    }

    fun save(tracker: Tracker) {
        transaction {
            Trackers.insert {
                it[userId] = tracker.userId
                it[calories] = tracker.calories
                it[drinking] = tracker.drinking
                it[walkHours] = tracker.walkHours
            }
        }
    }

    fun delete(id: Int) {
        transaction {
            Trackers.deleteWhere { Trackers.id eq id }
        }
    }

    fun findByUserId(userId: Int): ArrayList<Tracker>{
        val trackerList: ArrayList<Tracker> = arrayListOf()
        transaction {
            Trackers.selectAll().where { Trackers.userId eq userId }.map {
                trackerList.add(mapToTracker(it)) }
        }
        return trackerList
    }

    fun update(id: Int, tracker: Tracker){
        transaction {
            Trackers.update({ Trackers.id eq id }) {
                it[calories] = tracker.calories
                it[drinking] = tracker.drinking
                it[walkHours] = tracker.walkHours
            }
        }
    }

}