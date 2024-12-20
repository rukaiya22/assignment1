package ie.setu.config

import ie.setu.controllers.TrackerController
import ie.setu.controllers.UserController
import ie.setu.domain.Tracker
import ie.setu.domain.User
import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import io.javalin.vue.VueComponent
import java.nio.file.Files
import java.nio.file.Paths

class JavalinConfig {
    val userController = UserController()
    val trackerController = TrackerController()

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
        app.get("/appointments", VueComponent("<appointments></appointments>"))




    }
}