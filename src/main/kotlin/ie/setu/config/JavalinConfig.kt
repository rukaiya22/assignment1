package ie.setu.config

import ie.setu.controllers.UserController
import ie.setu.controllers.TrackerController
import ie.setu.controllers.DietController
import ie.setu.controllers.ExerciseController
import ie.setu.controllers.RestController
import ie.setu.controllers.BiometricController
import ie.setu.controllers.SupplementController
import ie.setu.controllers.SportController
import ie.setu.controllers.AppointmentController
import ie.setu.domain.User
import ie.setu.domain.Tracker
import ie.setu.domain.Diet
import ie.setu.domain.Exercise
import ie.setu.domain.Rest
import ie.setu.domain.Biometric
import ie.setu.domain.Supplement
import ie.setu.domain.Sport
import ie.setu.domain.Appointment
import io.javalin.Javalin
import io.javalin.vue.VueComponent
import java.nio.file.Files
import java.nio.file.Paths

class JavalinConfig {
    val userController = UserController()
    val trackerController = TrackerController()
    val dietController = DietController()
    val exerciseController = ExerciseController()
    val restController = RestController()
    val biometricController = BiometricController()
    val supplementController = SupplementController()
    val sportController = SportController()
    val appointmentController = AppointmentController()

    fun startJavalinService(): Javalin {

        val app = Javalin.create{
                config -> config.staticFiles.add("/static")
                config.staticFiles.enableWebjars()
                config.vue.vueInstanceNameInJs = "app" // only required for Vue 3, is defined in layout.html
        }.apply {
            exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(7000)

        registerRoutes(app)
        return app
    }

    private fun registerRoutes(app: Javalin) {

        // ############ API for users feature #######################
        // Getting all users. Inject DAO into the controller
        app.get("/api/users") { ctx ->
            ctx.json(userController.getAllUsers())
        }

        //adding new user
        app.post("api/users") { ctx ->
            val user = ctx.bodyAsClass(User::class.java)
            if(userController.createUser(user)!= null){
                ctx.json(user)
                ctx.status(201)
            }else{
                ctx.result("User creation failed")
                ctx.status(400)
            }
        }

        //updating a user
        app.put("api/users") { ctx ->
            val user = ctx.bodyAsClass(User::class.java)
            if(userController.updateUser(user)!= null){
                ctx.json(user)
                ctx.status(201)
            }else{
                ctx.result("User creation failed")
                ctx.status(400)
            }
        }

        //get a user by id
        app.get("/api/users/{id}") { ctx ->
            val id = ctx.pathParamAsClass<Int>("id", Int::class.java).get()
            var user = userController.getUserById(id)
            if(user != null) {
                ctx.json(user)
                ctx.status(200)
            }else{
                ctx.status(404)
            }
        }

        //get a user by email
        app.get("/api/users/email/{email}") { ctx ->
            val email = ctx.pathParamAsClass<String>("email", String::class.java).get()
            var user = userController.getUserByEmail(email)
            if(user != null) {
                ctx.json(user)
                ctx.status(200)
            }else{
                ctx.status(404)
            }
        }


        //delete a user
        app.delete("api/users/{user-id}") { ctx ->
            val userId = ctx.pathParamAsClass<Int>("user-id", Int::class.java).get()
            ctx.status(userController.deleteUser(userId))
        }

        // ############ API for trackers feature #######################
        //getting tracker by users
        app.get("api/trackers/{user-id}") { ctx ->
            val userId = ctx.pathParamAsClass<Int>("user-id", Int::class.java).get()
            val trackers = trackerController.getTrackersByUserId(userId)
            if(trackers.size == 0) {
                ctx.status(404)
            }else{
                ctx.json(trackers)
                ctx.status(200)
            }

        }

        //adding a tracker
        app.post("api/trackers") { ctx ->
            val tracker = ctx.bodyAsClass(Tracker::class.java)
            ctx.json(trackerController.createTracker(tracker))
        }

        //updating a tracker
        app.put("api/trackers") { ctx ->
            val tracker = ctx.bodyAsClass(Tracker::class.java)
            if(trackerController.updateTracker(tracker)!= null){
                ctx.json(tracker)
                ctx.status(201)
            }else{
                ctx.result("User creation failed")
                ctx.status(400)
            }
        }

        //delete a tracker
        app.delete("api/trackers/{tracker-id}") { ctx ->
            val trackerId = ctx.pathParamAsClass<Int>("tracker-id", Int::class.java).get()
            ctx.status(trackerController.deletedTracker(trackerId))
        }

        // ############ API for diets feature #######################
        //getting diet by users
        app.get("api/diets/{user-id}") { ctx ->
            val userId = ctx.pathParamAsClass<Int>("user-id", Int::class.java).get()
            val diets = dietController.getDietsByUserId(userId)
            if(diets.size == 0) {
                ctx.status(404)
            }else{
                ctx.json(diets)
                ctx.status(200)
            }

        }

        //adding a diet
        app.post("api/diets") { ctx ->
            val diet = ctx.bodyAsClass(Diet::class.java)
            ctx.json(dietController.createDiet(diet))
        }

        //updating a diet
        app.put("api/diets") { ctx ->
            val diet = ctx.bodyAsClass(Diet::class.java)
            if(dietController.updateDiet(diet)!= null){
                ctx.json(diet)
                ctx.status(201)
            }else{
                ctx.result("User creation failed")
                ctx.status(400)
            }
        }

        //delete a diet
        app.delete("api/diets/{diet-id}") { ctx ->
            val dietId = ctx.pathParamAsClass<Int>("diet-id", Int::class.java).get()
            ctx.status(dietController.deletedDiet(dietId))
        }

        // ############ API for exercises feature #######################
        //getting exercise by users
        app.get("api/exercises/{user-id}") { ctx ->
            val userId = ctx.pathParamAsClass<Int>("user-id", Int::class.java).get()
            val exercises = exerciseController.getExercisesByUserId(userId)
            if(exercises.size == 0) {
                ctx.status(404)
            }else{
                ctx.json(exercises)
                ctx.status(200)
            }

        }

        //adding a exercise
        app.post("api/exercises") { ctx ->
            val exercise = ctx.bodyAsClass(Exercise::class.java)
            ctx.json(exerciseController.createExercise(exercise))
        }

        //updating a exercise
        app.put("api/exercises") { ctx ->
            val exercise = ctx.bodyAsClass(Exercise::class.java)
            if(exerciseController.updateExercise(exercise)!= null){
                ctx.json(exercise)
                ctx.status(201)
            }else{
                ctx.result("User creation failed")
                ctx.status(400)
            }
        }

        //delete a exercise
        app.delete("api/exercises/{exercise-id}") { ctx ->
            val exerciseId = ctx.pathParamAsClass<Int>("exercise-id", Int::class.java).get()
            ctx.status(exerciseController.deletedExercise(exerciseId))
        }


        // ############ API for rests feature #######################
        //getting rest by users
        app.get("api/rests/{user-id}") { ctx ->
            val userId = ctx.pathParamAsClass<Int>("user-id", Int::class.java).get()
            val rests = restController.getRestsByUserId(userId)
            if(rests.size == 0) {
                ctx.status(404)
            }else{
                ctx.json(rests)
                ctx.status(200)
            }

        }

        //adding a rest
        app.post("api/rests") { ctx ->
            val rest = ctx.bodyAsClass(Rest::class.java)
            ctx.json(restController.createRest(rest))
        }

        //updating a rest
        app.put("api/rests") { ctx ->
            val rest = ctx.bodyAsClass(Rest::class.java)
            if(restController.updateRest(rest)!= null){
                ctx.json(rest)
                ctx.status(201)
            }else{
                ctx.result("User creation failed")
                ctx.status(400)
            }
        }

        //delete a rest
        app.delete("api/rests/{rest-id}") { ctx ->
            val restId = ctx.pathParamAsClass<Int>("rest-id", Int::class.java).get()
            ctx.status(restController.deletedRest(restId))
        }

        // ############ API for biometrics feature #######################
        //getting biometric by users
        app.get("api/biometrics/{user-id}") { ctx ->
            val userId = ctx.pathParamAsClass<Int>("user-id", Int::class.java).get()
            val biometrics = biometricController.getBiometricsByUserId(userId)
            if(biometrics.size == 0) {
                ctx.status(404)
            }else{
                ctx.json(biometrics)
                ctx.status(200)
            }

        }

        //adding a biometric
        app.post("api/biometrics") { ctx ->
            val biometric = ctx.bodyAsClass(Biometric::class.java)
            ctx.json(biometricController.createBiometric(biometric))
        }

        //updating a biometric
        app.put("api/biometrics") { ctx ->
            val biometric = ctx.bodyAsClass(Biometric::class.java)
            if(biometricController.updateBiometric(biometric)!= null){
                ctx.json(biometric)
                ctx.status(201)
            }else{
                ctx.result("User creation failed")
                ctx.status(400)
            }
        }

        //delete a biometric
        app.delete("api/biometrics/{biometric-id}") { ctx ->
            val biometricId = ctx.pathParamAsClass<Int>("biometric-id", Int::class.java).get()
            ctx.status(biometricController.deletedBiometric(biometricId))
        }

        // ############ API for supplements feature #######################
        //getting supplement by users
        app.get("api/supplements/{user-id}") { ctx ->
            val userId = ctx.pathParamAsClass<Int>("user-id", Int::class.java).get()
            val supplements = supplementController.getSupplementsByUserId(userId)
            if(supplements.size == 0) {
                ctx.status(404)
            }else{
                ctx.json(supplements)
                ctx.status(200)
            }

        }

        //adding a supplement
        app.post("api/supplements") { ctx ->
            val supplement = ctx.bodyAsClass(Supplement::class.java)
            ctx.json(supplementController.createSupplement(supplement))
        }

        //updating a supplement
        app.put("api/supplements") { ctx ->
            val supplement = ctx.bodyAsClass(Supplement::class.java)
            if(supplementController.updateSupplement(supplement)!= null){
                ctx.json(supplement)
                ctx.status(201)
            }else{
                ctx.result("User creation failed")
                ctx.status(400)
            }
        }

        //delete a supplement
        app.delete("api/supplements/{supplement-id}") { ctx ->
            val supplementId = ctx.pathParamAsClass<Int>("supplement-id", Int::class.java).get()
            ctx.status(supplementController.deletedSupplement(supplementId))
        }

        // ############ API for sports feature #######################
        //getting sport by users
        app.get("api/sports/{user-id}") { ctx ->
            val userId = ctx.pathParamAsClass<Int>("user-id", Int::class.java).get()
            val sports = sportController.getSportsByUserId(userId)
            if(sports.size == 0) {
                ctx.status(404)
            }else{
                ctx.json(sports)
                ctx.status(200)
            }

        }

        //adding a sport
        app.post("api/sports") { ctx ->
            val sport = ctx.bodyAsClass(Sport::class.java)
            ctx.json(sportController.createSport(sport))
        }

        //updating a sport
        app.put("api/sports") { ctx ->
            val sport = ctx.bodyAsClass(Sport::class.java)
            if(sportController.updateSport(sport)!= null){
                ctx.json(sport)
                ctx.status(201)
            }else{
                ctx.result("User creation failed")
                ctx.status(400)
            }
        }

        //delete a sport
        app.delete("api/sports/{sport-id}") { ctx ->
            val sportId = ctx.pathParamAsClass<Int>("sport-id", Int::class.java).get()
            ctx.status(sportController.deletedSport(sportId))
        }


        // ############ API for appointments feature #######################
        //getting appointment by users
        app.get("api/appointments/{user-id}") { ctx ->
            val userId = ctx.pathParamAsClass<Int>("user-id", Int::class.java).get()
            val appointments = appointmentController.getAppointmentsByUserId(userId)
            if(appointments.size == 0) {
                ctx.status(404)
            }else{
                ctx.json(appointments)
                ctx.status(200)
            }

        }

        //adding a appointment
        app.post("api/appointments") { ctx ->
            val appointment = ctx.bodyAsClass(Appointment::class.java)
            ctx.json(appointmentController.createAppointment(appointment))
        }

        //updating a appointment
        app.put("api/appointments") { ctx ->
            val appointment = ctx.bodyAsClass(Appointment::class.java)
            if(appointmentController.updateAppointment(appointment)!= null){
                ctx.json(appointment)
                ctx.status(201)
            }else{
                ctx.result("User creation failed")
                ctx.status(400)
            }
        }

        //delete a appointment
        app.delete("api/appointments/{appointment-id}") { ctx ->
            val appointmentId = ctx.pathParamAsClass<Int>("appointment-id", Int::class.java).get()
            ctx.status(appointmentController.deletedAppointment(appointmentId))
        }




        //end point to get swagger JSON file
        app.get("/swagger-docs") { ctx ->
            val swaggerPath = Paths.get("src/main/resources/swagger.json")
            val swaggerContent = Files.readString(swaggerPath)
            ctx.contentType("application/json").result(swaggerContent)
        }

        // for vue front end
        app.get("/", VueComponent("<home-page></home-page>"))

        app.get("/users", VueComponent("<users></users>"))
        app.get("/trackers", VueComponent("<trackers></trackers>"))
        app.get("/diets", VueComponent("<diets></diets>"))
        app.get("/exercises", VueComponent("<exercises></exercises>"))
        app.get("/rests", VueComponent("<rests></rests>"))
        app.get("/biometrics", VueComponent("<biometrics></biometrics>"))
        app.get("/supplements", VueComponent("<supplements></supplements>"))
        app.get("/sports", VueComponent("<sports></sports>"))
        app.get("/appointments", VueComponent("<appointments></appointments>"))




    }
}