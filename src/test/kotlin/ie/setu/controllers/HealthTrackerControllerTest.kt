package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.config.DbConfig
import ie.setu.domain.User
import ie.setu.helpers.ServerContainer
import org.junit.jupiter.api.TestInstance
import kong.unirest.core.Unirest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kong.unirest.core.HttpResponse
import kong.unirest.core.JsonNode
import ie.setu.helpers.validEmail
import ie.setu.helpers.validName
import org.junit.jupiter.api.Nested

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthTrackerControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

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
    }

    @Nested
    inner class TrackerAPI{

        @Test
        fun `get all trackers using a userId from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/trackers/1").asString()
            assertEquals(200, response.status)
        }

    }


    // Generic function to convert JSON string to an object
    inline fun <reified T> jsonToObject(json: String): T {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(json)
    }





}