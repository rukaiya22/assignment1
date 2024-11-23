package ie.setu.utils

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.domain.Tracker
import ie.setu.domain.db.Trackers
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToTracker(it: ResultRow) = Tracker(
    id = it[Trackers.id],
    userId = it[Trackers.userId],
    calories = it[Trackers.calories],
    drinking = it[Trackers.drinking],
    walkHours = it[Trackers.walkHours]
)

