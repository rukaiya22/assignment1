package ie.setu.utils

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.domain.Tracker
import ie.setu.domain.db.Trackers
import ie.setu.domain.Diet
import ie.setu.domain.db.Diets
import ie.setu.domain.Exercise
import ie.setu.domain.db.Exercises
import ie.setu.domain.Rest
import ie.setu.domain.db.Rests
import ie.setu.domain.Biometric
import ie.setu.domain.db.Biometrics
import ie.setu.domain.Supplement
import ie.setu.domain.db.Supplements
import ie.setu.domain.Sport
import ie.setu.domain.db.Sports
import ie.setu.domain.Appointment
import ie.setu.domain.db.Appointments

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

fun mapToDiet(it: ResultRow) = Diet(
    id = it[Diets.id],
    userId = it[Diets.userId],
    carbohydrate = it[Diets.carbohydrate],
    protein = it[Diets.protein],
    fat = it[Diets.fat]
)

fun mapToExercise(it: ResultRow) = Exercise(
    id = it[Exercises.id],
    userId = it[Exercises.userId],
    running = it[Exercises.running],
    swimming = it[Exercises.swimming],
    cycling = it[Exercises.cycling],
    equipment_based = it[Exercises.equipment_based]
)

fun mapToRest(it: ResultRow) = Rest(
    id = it[Rests.id],
    userId = it[Rests.userId],
    sleep = it[Rests.sleep],
    power_nap = it[Rests.power_nap],
    meditation = it[Rests.meditation]
)

fun mapToBiometric(it: ResultRow) = Biometric(
    id = it[Biometrics.id],
    userId = it[Biometrics.userId],
    bp_systolic = it[Biometrics.bp_systolic],
    bp_diastolic = it[Biometrics.bp_diastolic],
    blood_sugar = it[Biometrics.blood_sugar],
    cholesterol = it[Biometrics.cholesterol]
)

fun mapToSupplement(it: ResultRow) = Supplement(
    id = it[Supplements.id],
    userId = it[Supplements.userId],
    vitamin_d = it[Supplements.vitamin_d],
    vitamin_c = it[Supplements.vitamin_c],
    iron = it[Supplements.iron],
    calcium = it[Supplements.calcium]
)

fun mapToSport(it: ResultRow) = Sport(
    id = it[Sports.id],
    userId = it[Sports.userId],
    sports_name = it[Sports.sports_name],
    playing_hours = it[Sports.playing_hours]
)

fun mapToAppointment(it: ResultRow) = Appointment(
    id = it[Appointments.id],
    userId = it[Appointments.userId],
    appointment_type = it[Appointments.appointment_type],
    appointment_date = it[Appointments.appointment_date]
)





