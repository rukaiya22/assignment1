package ie.setu.helpers

import ie.setu.domain.Tracker
import ie.setu.domain.User
import ie.setu.domain.Diet
import ie.setu.domain.Exercise
import ie.setu.domain.Rest
import ie.setu.domain.Biometric
import ie.setu.domain.Supplement
import ie.setu.domain.Sport

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val trackers = arrayListOf<Tracker>(
    Tracker(userId = 1, calories = 100.0, drinking = 5.3, walkHours = 3.5, id = 1),
    Tracker(userId = 1, calories = 80.0, drinking = 6.3, walkHours = 0.2, id = 2),
    Tracker(userId = 1, calories = 150.0, drinking = 4.8, walkHours = 4.0, id = 3),
    Tracker(userId = 1, calories = 185.0, drinking = 3.5, walkHours = 3.2, id = 4),

    Tracker(userId = 2, calories = 133.0, drinking = 3.8, walkHours = 12.5, id = 5),
    Tracker(userId = 2, calories = 122.0, drinking = 7.5, walkHours = 5.6, id = 6),

    Tracker(userId = 3, calories = 101.0, drinking = 2.8, walkHours = 8.0, id = 7),
    Tracker(userId = 3, calories = 105.0, drinking = 3.5, walkHours = 6.2, id = 8),

    Tracker(userId = 4, calories = 200.0, drinking = 6.8, walkHours = 13.0, id = 9),
    Tracker(userId = 4, calories = 205.0, drinking = 6.5, walkHours = 5.2, id = 10),

)

val diets = arrayListOf<Diet>(
    Diet(userId = 1, carbohydrate = 100.0, protein = 5.3, fat = 3.5, id = 1),
    Diet(userId = 1, carbohydrate = 80.0, protein = 6.3, fat = 0.2, id = 2),
    Diet(userId = 1, carbohydrate = 150.0, protein = 4.8, fat = 4.0, id = 3),
    Diet(userId = 1, carbohydrate = 185.0, protein = 3.5, fat = 3.2, id = 4),

    Diet(userId = 2, carbohydrate = 133.0, protein = 3.8, fat = 12.5, id = 5),
    Diet(userId = 2, carbohydrate = 122.0, protein = 7.5, fat = 5.6, id = 6),

    Diet(userId = 3, carbohydrate = 101.0, protein = 2.8, fat = 8.0, id = 7),
    Diet(userId = 3, carbohydrate = 105.0, protein = 3.5, fat = 6.2, id = 8),

    Diet(userId = 4, carbohydrate = 200.0, protein = 6.8, fat = 13.0, id = 9),
    Diet(userId = 4, carbohydrate = 205.0, protein = 6.5, fat = 5.2, id = 10),

)

val exercises = arrayListOf<Exercise>(
    Exercise(userId = 1, running = 100.0, swimming = 5.3, cycling = 3.5, equipment_based = 3.5, id = 1),
    Exercise(userId = 1, running = 80.0, swimming = 6.3, cycling = 0.2, equipment_based = 3.5, id = 2),
    Exercise(userId = 1, running = 150.0, swimming = 4.8, cycling = 4.0, equipment_based = 3.5, id = 3),
    Exercise(userId = 1, running = 185.0, swimming = 3.5, cycling = 3.2, equipment_based = 3.5, id = 4),

    Exercise(userId = 2, running = 133.0, swimming = 3.8, cycling = 12.5, equipment_based = 3.5, id = 5),
    Exercise(userId = 2, running = 122.0, swimming = 7.5, cycling = 5.6, equipment_based = 3.5, id = 6),

    Exercise(userId = 3, running = 101.0, swimming = 2.8, cycling = 8.0, equipment_based = 3.5, id = 7),
    Exercise(userId = 3, running = 105.0, swimming = 3.5, cycling = 6.2, equipment_based = 3.5, id = 8),

    Exercise(userId = 4, running = 200.0, swimming = 6.8, cycling = 13.0, equipment_based = 3.5, id = 9),
    Exercise(userId = 4, running = 205.0, swimming = 6.5, cycling = 5.2, equipment_based = 3.5, id = 10),

)

val rests = arrayListOf<Rest>(
    Rest(userId = 1, sleep = 100.0, power_nap = 5.3, meditation = 3.5, id = 1),
    Rest(userId = 1, sleep = 80.0, power_nap = 6.3, meditation = 0.2, id = 2),
    Rest(userId = 1, sleep = 150.0, power_nap = 4.8, meditation = 4.0, id = 3),
    Rest(userId = 1, sleep = 185.0, power_nap = 3.5, meditation = 3.2, id = 4),

    Rest(userId = 2, sleep = 133.0, power_nap = 3.8, meditation = 12.5, id = 5),
    Rest(userId = 2, sleep = 122.0, power_nap = 7.5, meditation = 5.6, id = 6),

    Rest(userId = 3, sleep = 101.0, power_nap = 2.8, meditation = 8.0, id = 7),
    Rest(userId = 3, sleep = 105.0, power_nap = 3.5, meditation = 6.2, id = 8),

    Rest(userId = 4, sleep = 200.0, power_nap = 6.8, meditation = 13.0, id = 9),
    Rest(userId = 4, sleep = 205.0, power_nap = 6.5, meditation = 5.2, id = 10),

)

val biometrics = arrayListOf<Biometric>(
    Biometric(userId = 1, bp_systolic = 100.0, bp_diastolic = 5.3, blood_sugar = 3.5, cholesterol = 3.5, id = 1),
    Biometric(userId = 1, bp_systolic = 80.0, bp_diastolic = 6.3, blood_sugar = 0.2, cholesterol = 3.5, id = 2),
    Biometric(userId = 1, bp_systolic = 150.0, bp_diastolic = 4.8, blood_sugar = 4.0, cholesterol = 3.5, id = 3),
    Biometric(userId = 1, bp_systolic = 185.0, bp_diastolic = 3.5, blood_sugar = 3.2, cholesterol = 3.5, id = 4),

    Biometric(userId = 2, bp_systolic = 133.0, bp_diastolic = 3.8, blood_sugar = 12.5, cholesterol = 3.5, id = 5),
    Biometric(userId = 2, bp_systolic = 122.0, bp_diastolic = 7.5, blood_sugar = 5.6, cholesterol = 3.5, id = 6),

    Biometric(userId = 3, bp_systolic = 101.0, bp_diastolic = 2.8, blood_sugar = 8.0, cholesterol = 3.5, id = 7),
    Biometric(userId = 3, bp_systolic = 105.0, bp_diastolic = 3.5, blood_sugar = 6.2, cholesterol = 3.5, id = 8),

    Biometric(userId = 4, bp_systolic = 200.0, bp_diastolic = 6.8, blood_sugar = 13.0, cholesterol = 3.5, id = 9),
    Biometric(userId = 4, bp_systolic = 205.0, bp_diastolic = 6.5, blood_sugar = 5.2, cholesterol = 3.5, id = 10),

)

val supplements = arrayListOf<Supplement>(
    Supplement(userId = 1, vitamin_d = 100.0, vitamin_c = 5.3, iron = 3.5, calcium = 3.5, id = 1),
    Supplement(userId = 1, vitamin_d = 80.0, vitamin_c = 6.3, iron = 0.2, calcium = 3.5, id = 2),
    Supplement(userId = 1, vitamin_d = 150.0, vitamin_c = 4.8, iron = 4.0, calcium = 3.5, id = 3),
    Supplement(userId = 1, vitamin_d = 185.0, vitamin_c = 3.5, iron = 3.2, calcium = 3.5, id = 4),

    Supplement(userId = 2, vitamin_d = 133.0, vitamin_c = 3.8, iron = 12.5, calcium = 3.5, id = 5),
    Supplement(userId = 2, vitamin_d = 122.0, vitamin_c = 7.5, iron = 5.6, calcium = 3.5, id = 6),

    Supplement(userId = 3, vitamin_d = 101.0, vitamin_c = 2.8, iron = 8.0, calcium = 3.5, id = 7),
    Supplement(userId = 3, vitamin_d = 105.0, vitamin_c = 3.5, iron = 6.2, calcium = 3.5, id = 8),

    Supplement(userId = 4, vitamin_d = 200.0, vitamin_c = 6.8, iron = 13.0, calcium = 3.5, id = 9),
    Supplement(userId = 4, vitamin_d = 205.0, vitamin_c = 6.5, iron = 5.2, calcium = 3.5, id = 10),

)

val sports = arrayListOf<Sport>(
    Sport(userId = 1, sports_name = "cricket", playing_hours = 5.3, id = 1),
    Sport(userId = 1, sports_name = "football", playing_hours = 6.3, id = 2),
    Sport(userId = 1, sports_name = "hurling", playing_hours = 4.8, id = 3),
    Sport(userId = 1, sports_name = "tennis", playing_hours = 3.5, id = 4),

    Sport(userId = 2, sports_name = "football", playing_hours = 3.8, id = 5),
    Sport(userId = 2, sports_name = "cricket", playing_hours = 7.5, id = 6),

    Sport(userId = 3, sports_name = "tennis", playing_hours = 2.8, id = 7),
    Sport(userId = 3, sports_name = "hurling", playing_hours = 3.5, id = 8),

    Sport(userId = 4, sports_name = "hurling", playing_hours = 6.8, id = 9),
    Sport(userId = 4, sports_name = "cricket", playing_hours = 6.5, id = 10),

)