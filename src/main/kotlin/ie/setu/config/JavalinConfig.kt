package ie.setu.config

import ie.setu.controllers.TrackerController
import ie.setu.controllers.UserController
import ie.setu.domain.Tracker
import ie.setu.domain.User
import io.javalin.Javalin
import java.nio.file.Files
import java.nio.file.Paths

class JavalinConfig {
    val userController = UserController()
    val trackerController = TrackerController()

    fun startJavalinService(): Javalin {

        val app = Javalin.create{
                config -> config.staticFiles.add("/static")
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

        //delete a user
        app.delete("api/users/{user-id}") { ctx ->
            val userId = ctx.pathParamAsClass<Int>("user-id", Int::class.java).get()
            if(trackerController.deleteTrackersByUserId(userId))
                ctx.json(userController.deleteUser(userId))
            else
                ctx.status(404)
        }

        //getting tracker by users
        app.get("api/trackers/{user-id}") { ctx ->
            val userId = ctx.pathParamAsClass<Int>("user-id", Int::class.java).get()
            val trackers = trackerController.getAllTrackers()
            val trackersToView = trackers.filter { it.userId == userId }
            ctx.json(trackersToView)
        }

        //adding a tracker
        app.post("api/trackers") { ctx ->
            val tracker = ctx.bodyAsClass(Tracker::class.java)
            ctx.json(trackerController.createTracker(tracker))
        }

        //delete a tracker
        app.delete("api/trackers/{tracker-id}") { ctx ->
            val trackerId = ctx.pathParamAsClass<Int>("tracker-id", Int::class.java).get()
            ctx.json(trackerController.deletedTracker(trackerId))
        }



        //end point to get swagger JSON file
        app.get("/swagger-docs") { ctx ->
            val swaggerPath = Paths.get("src/main/resources/swagger.json")
            val swaggerContent = Files.readString(swaggerPath)
            ctx.contentType("application/json").result(swaggerContent)
        }
    }
}