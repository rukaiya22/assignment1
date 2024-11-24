package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.config.DbConfig
import ie.setu.domain.Tracker
import ie.setu.domain.User
import ie.setu.helpers.DatabaseHelper
import ie.setu.helpers.ServerContainer
import kong.unirest.core.Unirest
import org.junit.jupiter.api.Assertions.assertEquals
import kong.unirest.core.HttpResponse
import kong.unirest.core.JsonNode
import ie.setu.helpers.validEmail
import ie.setu.helpers.validName
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthTrackerControllerTest {

    private val db = DbConfig().getDbConnection(true)
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @BeforeAll
    fun setup() {
        DatabaseHelper.setupTestDatabase(db)
    }

    @AfterAll
    fun teardown() {
        DatabaseHelper.cleanupTestDatabase(db)
    }

    @Nested
    inner class UserAPI{
        @Test
        fun `get all users from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/users/").asString()
            assertEquals(200, response.status)
        }

        @Test
        fun `get user by id when user does not exist returns 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/users/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        //helper function to add a test user to the database
        private fun addUser (name: String, email: String): HttpResponse<JsonNode> {
            return Unirest.post(origin + "/api/users")
                .body("{\"name\":\"$name\", \"email\":\"$email\"}")
                .asJson()
        }

        //helper function to delete a test user from the database
        private fun deleteUser (id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/users/$id").asString()
        }

        //helper function to retrieve a test user from the database by email
        private fun retrieveUserByEmail(email : String) : HttpResponse<String> {
            return Unirest.get(origin + "/api/users/email/${email}").asString()
        }

        //helper function to retrieve a test user from the database by id
        private fun retrieveUserById(id: Int) : HttpResponse<String> {
            return Unirest.get(origin + "/api/users/${id}").asString()
        }

        @Test
        fun `add a user with correct details returns a 201 response`() {

            //Arrange & Act & Assert
            //    add the user and verify return code (using fixture data)
            val addResponse = addUser(validName, validEmail)
            assertEquals(201, addResponse.status)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse= retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //Assert - verify the contents of the retrieved user
            val retrievedUser : User = jsonToObject(addResponse.body.toString())
            assertEquals(validEmail, retrievedUser.email)
            assertEquals(validName, retrievedUser.name)

            //After - restore the db to previous state by deleting the added user
            val deleteResponse = deleteUser(retrievedUser.id)
            assertEquals(204, deleteResponse.status)
        }

    }

    @Nested
    inner class TrackerAPI{

        @Test
        fun `get a trackers by  using a userId that exists in the database returns 200 `() {
            val response = Unirest.get(origin + "/api/trackers/1").asString()
            assertEquals(200, response.status)

        }

        @Test
        fun `get trackers by user id when user does not exist returns empty value trackers and 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/trackers/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        //helper function to add a test tracker to the database
        private fun addTracker (userId: Int, calories: Double, drinking: Double, walkHours: Double): HttpResponse<JsonNode> {
            return Unirest.post(origin + "/api/trackers")
                .body("{\"userId\":$userId, \"calories\":$calories, \"drinking\":$drinking, \"walkHours\":$walkHours}")
                .asJson()
        }

        //helper function to delete a tracker user from the database
        private fun deleteTracker (id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/trackers/$id").asString()
        }


        @Test
        fun `adding and deleting a tracker to for an existing user`() {
            val tracker1 : ArrayList<Tracker> = jsonToObject( Unirest.get(origin + "/api/trackers/1").asString().body.toString())
            var total_trackers = tracker1.size
            deleteTracker(1)
            val tracker1_2 : ArrayList<Tracker> = jsonToObject( Unirest.get(origin + "/api/trackers/1").asString().body.toString())
            assertEquals(total_trackers-1, tracker1_2.size)
            addTracker(1, 0.0, 0.0, 0.0)
            val tracker1_1 : ArrayList<Tracker> = jsonToObject( Unirest.get(origin + "/api/trackers/1").asString().body.toString())
            assertEquals(total_trackers, tracker1_1.size)
        }


    }


    // Generic function to convert JSON string to an object
    inline fun <reified T> jsonToObject(json: String): T {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(json)
    }





}