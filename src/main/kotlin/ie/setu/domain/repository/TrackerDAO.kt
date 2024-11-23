package ie.setu.domain.repository

import ie.setu.domain.Tracker
import ie.setu.domain.db.Trackers
import ie.setu.utils.mapToTracker
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

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

    fun deleteByUserId(userId: Int) {
        transaction {
            Trackers.deleteWhere { Trackers.userId eq userId }
        }
    }

}