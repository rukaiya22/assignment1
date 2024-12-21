package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.config.DbConfig
import ie.setu.domain.Tracker
import ie.setu.domain.Diet
import ie.setu.domain.Exercise
import ie.setu.domain.Rest
import ie.setu.domain.Biometric
import ie.setu.domain.Suppliment
import ie.setu.domain.Sport
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
import kotlin.test.assertNotEquals

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

        //helper function to update a test user in the database
        private fun updateUser (id: Int, name: String, email: String): HttpResponse<JsonNode> {
            return Unirest.put(origin + "/api/users")
                .body("{\"id\":$id,\"name\":\"$name\", \"email\":\"$email\"}")
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
            val retrievedUser : User = jsonToObject(retrieveResponse.body.toString())
            assertEquals(validEmail, retrievedUser.email)
            assertEquals(validName, retrievedUser.name)

            //After - restore the db to previous state by deleting the added user
            val deleteResponse = deleteUser(retrievedUser.id)
            assertEquals(204, deleteResponse.status)
        }

        @Test
        fun `update a user with correct details returns a 201 response`() {

            //Arrange & Act & Assert
            //    add the user and verify return code (using fixture data)
            val addResponse = addUser(validName, validEmail)
            assertEquals(201, addResponse.status)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse= retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //Assert - verify the contents of the retrieved user
            val retrievedUser : User = jsonToObject(retrieveResponse.body.toString())
            assertEquals(validEmail, retrievedUser.email)
            assertEquals(validName, retrievedUser.name)

            //After - update the user email
            val id = retrievedUser.id
            val newEmail = "new_" + validEmail
            val newName = "new_" + validName
            val updateResponse = updateUser(id, newName, newEmail)
            assertEquals(201, updateResponse.status)

            //retrieve the updated user
            val retrieveUpdateUserResponse= retrieveUserByEmail(newEmail)
            assertEquals(200, retrieveUpdateUserResponse.status)

            //Assert - verify the contents of the retrieved updated user
            val retrievedUpdatedUser : User = jsonToObject(retrieveUpdateUserResponse.body.toString())
            assertEquals(newEmail, retrievedUpdatedUser.email)
            assertEquals(newName, retrievedUpdatedUser.name)
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

        //helper function to update a test tracker to the database
        private fun updateTracker (id: Int, userId: Int, calories: Double, drinking: Double, walkHours: Double): HttpResponse<JsonNode> {
            return Unirest.put(origin + "/api/trackers")
                .body("{\"id\":$id, \"userId\":$userId, \"calories\":$calories, \"drinking\":$drinking, \"walkHours\":$walkHours}")
                .asJson()
        }

        //helper function to delete a tracker user from the database
        private fun deleteTracker (id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/trackers/$id").asString()
        }


        @Test
        fun `adding and deleting a tracker for an existing user`() {
            val tracker1 : ArrayList<Tracker> = jsonToObject( Unirest.get(origin + "/api/trackers/1").asString().body.toString())
            var total_trackers = tracker1.size
            deleteTracker(1)
            val tracker1_2 : ArrayList<Tracker> = jsonToObject( Unirest.get(origin + "/api/trackers/1").asString().body.toString())
            assertEquals(total_trackers-1, tracker1_2.size)
            addTracker(1, 0.0, 0.0, 0.0)
            val tracker1_1 : ArrayList<Tracker> = jsonToObject( Unirest.get(origin + "/api/trackers/1").asString().body.toString())
            assertEquals(total_trackers, tracker1_1.size)
        }

        @Test
        fun `adding and updating a tracker for an existing user`() {
            val userId = 1
            val trackers : ArrayList<Tracker> = jsonToObject( Unirest.get(origin + "/api/trackers/$userId").asString().body.toString())
            val tracker = trackers[0]
            val id = tracker.id
            val oldcalories = tracker.calories
            val newCalories = oldcalories+50
            updateTracker(id, calories = newCalories, drinking = tracker.drinking, walkHours = tracker.walkHours, userId = userId)
            val newTrackers : ArrayList<Tracker> = jsonToObject( Unirest.get(origin + "/api/trackers/$userId").asString().body.toString())
            assertEquals(newTrackers[0].calories, newCalories)
            assertNotEquals(newTrackers[0].calories,oldcalories)
        }


    }

    @Nested
    inner class DietAPI{

        @Test
        fun `get a diets by  using a userId that exists in the database returns 200 `() {
            val response = Unirest.get(origin + "/api/diets/1").asString()
            assertEquals(200, response.status)

        }

        @Test
        fun `get diets by user id when user does not exist returns empty value diets and 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/diets/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        //helper function to add a test diet to the database
        private fun addDiet (userId: Int, carbohydrate: Double, protein: Double, fat: Double): HttpResponse<JsonNode> {
            return Unirest.post(origin + "/api/diets")
                .body("{\"userId\":$userId, \"carbohydrate\":$carbohydrate, \"protein\":$protein, \"fat\":$fat}")
                .asJson()
        }

        //helper function to update a test diet to the database
        private fun updateDiet (id: Int, userId: Int, carbohydrate: Double, protein: Double, fat: Double): HttpResponse<JsonNode> {
            return Unirest.put(origin + "/api/diets")
                .body("{\"id\":$id, \"userId\":$userId, \"carbohydrate\":$carbohydrate, \"protein\":$protein, \"fat\":$fat}")
                .asJson()
        }

        //helper function to delete a diet user from the database
        private fun deleteDiet (id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/diets/$id").asString()
        }


        @Test
        fun `adding and deleting a diet for an existing user`() {
            val diet1 : ArrayList<Diet> = jsonToObject( Unirest.get(origin + "/api/diets/1").asString().body.toString())
            var total_diets = diet1.size
            deleteDiet(1)
            val diet1_2 : ArrayList<Diet> = jsonToObject( Unirest.get(origin + "/api/diets/1").asString().body.toString())
            assertEquals(total_diets-1, diet1_2.size)
            addDiet(1, 0.0, 0.0, 0.0)
            val diet1_1 : ArrayList<Diet> = jsonToObject( Unirest.get(origin + "/api/diets/1").asString().body.toString())
            assertEquals(total_diets, diet1_1.size)
        }

        @Test
        fun `adding and updating a diet for an existing user`() {
            val userId = 1
            val diets : ArrayList<Diet> = jsonToObject( Unirest.get(origin + "/api/diets/$userId").asString().body.toString())
            val diet = diets[0]
            val id = diet.id
            val oldcarbohydrate = diet.carbohydrate
            val newCarbohydrate = oldcarbohydrate+50
            updateDiet(id, carbohydrate = newCarbohydrate, protein = diet.protein, fat = diet.fat, userId = userId)
            val newDiets : ArrayList<Diet> = jsonToObject( Unirest.get(origin + "/api/diets/$userId").asString().body.toString())
            assertEquals(newDiets[0].carbohydrate, newCarbohydrate)
            assertNotEquals(newDiets[0].carbohydrate,oldcarbohydrate)
        }


    }

    @Nested
    inner class ExerciseAPI{

        @Test
        fun `get a exercises by  using a userId that exists in the database returns 200 `() {
            val response = Unirest.get(origin + "/api/exercises/1").asString()
            assertEquals(200, response.status)

        }

        @Test
        fun `get exercises by user id when user does not exist returns empty value exercises and 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/exercises/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        //helper function to add a test exercise to the database
        private fun addExercise (userId: Int, running: Double, swimming: Double, cycling: Double, equipment_based: Double): HttpResponse<JsonNode> {
            return Unirest.post(origin + "/api/exercises")
                .body("{\"userId\":$userId, \"running\":$running, \"swimming\":$swimming, \"cycling\":$cycling, \"equipment_based\":$equipment_based}")
                .asJson()
        }

        //helper function to update a test exercise to the database
        private fun updateExercise (id: Int, userId: Int, running: Double, swimming: Double, cycling: Double, equipment_based: Double): HttpResponse<JsonNode> {
            return Unirest.put(origin + "/api/exercises")
                .body("{\"id\":$id, \"userId\":$userId, \"running\":$running, \"swimming\":$swimming, \"cycling\":$cycling, \"equipment_based\":$equipment_based}")
                .asJson()
        }

        //helper function to delete a exercise user from the database
        private fun deleteExercise (id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/exercises/$id").asString()
        }


        @Test
        fun `adding and deleting a exercise for an existing user`() {
            val exercise1 : ArrayList<Exercise> = jsonToObject( Unirest.get(origin + "/api/exercises/1").asString().body.toString())
            var total_exercises = exercise1.size
            deleteExercise(1)
            val exercise1_2 : ArrayList<Exercise> = jsonToObject( Unirest.get(origin + "/api/exercises/1").asString().body.toString())
            assertEquals(total_exercises-1, exercise1_2.size)
            addExercise(1, 0.0, 0.0, 0.0, 0.0)
            val exercise1_1 : ArrayList<Exercise> = jsonToObject( Unirest.get(origin + "/api/exercises/1").asString().body.toString())
            assertEquals(total_exercises, exercise1_1.size)
        }

        @Test
        fun `adding and updating a exercise for an existing user`() {
            val userId = 1
            val exercises : ArrayList<Exercise> = jsonToObject( Unirest.get(origin + "/api/exercises/$userId").asString().body.toString())
            val exercise = exercises[0]
            val id = exercise.id
            val oldrunning = exercise.running
            val newRunning = oldrunning+50
            updateExercise(id, running = newRunning, swimming = exercise.swimming, cycling = exercise.cycling, equipment_based = exercise.equipment_based, userId = userId)
            val newExercises : ArrayList<Exercise> = jsonToObject( Unirest.get(origin + "/api/exercises/$userId").asString().body.toString())
            assertEquals(newExercises[0].running, newRunning)
            assertNotEquals(newExercises[0].running,oldrunning)
        }


    }

    @Nested
    inner class RestAPI{

        @Test
        fun `get a rests by  using a userId that exists in the database returns 200 `() {
            val response = Unirest.get(origin + "/api/rests/1").asString()
            assertEquals(200, response.status)

        }

        @Test
        fun `get rests by user id when user does not exist returns empty value rests and 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/rests/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        //helper function to add a test rest to the database
        private fun addRest (userId: Int, sleep: Double, power_nap: Double, meditation: Double): HttpResponse<JsonNode> {
            return Unirest.post(origin + "/api/rests")
                .body("{\"userId\":$userId, \"sleep\":$sleep, \"power_nap\":$power_nap, \"meditation\":$meditation}")
                .asJson()
        }

        //helper function to update a test rest to the database
        private fun updateRest (id: Int, userId: Int, sleep: Double, power_nap: Double, meditation: Double): HttpResponse<JsonNode> {
            return Unirest.put(origin + "/api/rests")
                .body("{\"id\":$id, \"userId\":$userId, \"sleep\":$sleep, \"power_nap\":$power_nap, \"meditation\":$meditation}")
                .asJson()
        }

        //helper function to delete a rest user from the database
        private fun deleteRest (id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/rests/$id").asString()
        }


        @Test
        fun `adding and deleting a rest for an existing user`() {
            val rest1 : ArrayList<Rest> = jsonToObject( Unirest.get(origin + "/api/rests/1").asString().body.toString())
            var total_rests = rest1.size
            deleteRest(1)
            val rest1_2 : ArrayList<Rest> = jsonToObject( Unirest.get(origin + "/api/rests/1").asString().body.toString())
            assertEquals(total_rests-1, rest1_2.size)
            addRest(1, 0.0, 0.0, 0.0)
            val rest1_1 : ArrayList<Rest> = jsonToObject( Unirest.get(origin + "/api/rests/1").asString().body.toString())
            assertEquals(total_rests, rest1_1.size)
        }

        @Test
        fun `adding and updating a rest for an existing user`() {
            val userId = 1
            val rests : ArrayList<Rest> = jsonToObject( Unirest.get(origin + "/api/rests/$userId").asString().body.toString())
            val rest = rests[0]
            val id = rest.id
            val oldsleep = rest.sleep
            val newSleep = oldsleep+50
            updateRest(id, sleep = newSleep, power_nap = rest.power_nap, meditation = rest.meditation, userId = userId)
            val newRests : ArrayList<Rest> = jsonToObject( Unirest.get(origin + "/api/rests/$userId").asString().body.toString())
            assertEquals(newRests[0].sleep, newSleep)
            assertNotEquals(newRests[0].sleep,oldsleep)
        }


    }

    @Nested
    inner class BiometricAPI{

        @Test
        fun `get a biometrics by  using a userId that exists in the database returns 200 `() {
            val response = Unirest.get(origin + "/api/biometrics/1").asString()
            assertEquals(200, response.status)

        }

        @Test
        fun `get biometrics by user id when user does not exist returns empty value biometrics and 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/biometrics/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        //helper function to add a test biometric to the database
        private fun addBiometric (userId: Int, bp_systolic: Double, bp_diastolic: Double, blood_sugar: Double, cholesterol: Double): HttpResponse<JsonNode> {
            return Unirest.post(origin + "/api/biometrics")
                .body("{\"userId\":$userId, \"bp_systolic\":$bp_systolic, \"bp_diastolic\":$bp_diastolic, \"blood_sugar\":$blood_sugar, \"cholesterol\":$cholesterol}")
                .asJson()
        }

        //helper function to update a test biometric to the database
        private fun updateBiometric (id: Int, userId: Int, bp_systolic: Double, bp_diastolic: Double, blood_sugar: Double, cholesterol: Double): HttpResponse<JsonNode> {
            return Unirest.put(origin + "/api/biometrics")
                .body("{\"id\":$id, \"userId\":$userId, \"bp_systolic\":$bp_systolic, \"bp_diastolic\":$bp_diastolic, \"blood_sugar\":$blood_sugar, \"cholesterol\":$cholesterol}")
                .asJson()
        }

        //helper function to delete a biometric user from the database
        private fun deleteBiometric (id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/biometrics/$id").asString()
        }


        @Test
        fun `adding and deleting a biometric for an existing user`() {
            val biometric1 : ArrayList<Biometric> = jsonToObject( Unirest.get(origin + "/api/biometrics/1").asString().body.toString())
            var total_biometrics = biometric1.size
            deleteBiometric(1)
            val biometric1_2 : ArrayList<Biometric> = jsonToObject( Unirest.get(origin + "/api/biometrics/1").asString().body.toString())
            assertEquals(total_biometrics-1, biometric1_2.size)
            addBiometric(1, 0.0, 0.0, 0.0, 0.0)
            val biometric1_1 : ArrayList<Biometric> = jsonToObject( Unirest.get(origin + "/api/biometrics/1").asString().body.toString())
            assertEquals(total_biometrics, biometric1_1.size)
        }

        @Test
        fun `adding and updating a biometric for an existing user`() {
            val userId = 1
            val biometrics : ArrayList<Biometric> = jsonToObject( Unirest.get(origin + "/api/biometrics/$userId").asString().body.toString())
            val biometric = biometrics[0]
            val id = biometric.id
            val oldbp_systolic = biometric.bp_systolic
            val newBp_systolic = oldbp_systolic+50
            updateBiometric(id, bp_systolic = newBp_systolic, bp_diastolic = biometric.bp_diastolic, blood_sugar = biometric.blood_sugar, cholesterol = biometric.cholesterol, userId = userId)
            val newBiometrics : ArrayList<Biometric> = jsonToObject( Unirest.get(origin + "/api/biometrics/$userId").asString().body.toString())
            assertEquals(newBiometrics[0].bp_systolic, newBp_systolic)
            assertNotEquals(newBiometrics[0].bp_systolic,oldbp_systolic)
        }


    }

    @Nested
    inner class SupplimentAPI{

        @Test
        fun `get a suppliments by  using a userId that exists in the database returns 200 `() {
            val response = Unirest.get(origin + "/api/suppliments/1").asString()
            assertEquals(200, response.status)

        }

        @Test
        fun `get suppliments by user id when user does not exist returns empty value suppliments and 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/suppliments/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        //helper function to add a test suppliment to the database
        private fun addSuppliment (userId: Int, vitamin_d: Double, vitamin_c: Double, iron: Double, calcium: Double): HttpResponse<JsonNode> {
            return Unirest.post(origin + "/api/suppliments")
                .body("{\"userId\":$userId, \"vitamin_d\":$vitamin_d, \"vitamin_c\":$vitamin_c, \"iron\":$iron, \"calcium\":$calcium}")
                .asJson()
        }

        //helper function to update a test suppliment to the database
        private fun updateSuppliment (id: Int, userId: Int, vitamin_d: Double, vitamin_c: Double, iron: Double, calcium: Double): HttpResponse<JsonNode> {
            return Unirest.put(origin + "/api/suppliments")
                .body("{\"id\":$id, \"userId\":$userId, \"vitamin_d\":$vitamin_d, \"vitamin_c\":$vitamin_c, \"iron\":$iron, \"calcium\":$calcium}")
                .asJson()
        }

        //helper function to delete a suppliment user from the database
        private fun deleteSuppliment (id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/suppliments/$id").asString()
        }


        @Test
        fun `adding and deleting a suppliment for an existing user`() {
            val suppliment1 : ArrayList<Suppliment> = jsonToObject( Unirest.get(origin + "/api/suppliments/1").asString().body.toString())
            var total_suppliments = suppliment1.size
            deleteSuppliment(1)
            val suppliment1_2 : ArrayList<Suppliment> = jsonToObject( Unirest.get(origin + "/api/suppliments/1").asString().body.toString())
            assertEquals(total_suppliments-1, suppliment1_2.size)
            addSuppliment(1, 0.0, 0.0, 0.0, 0.0)
            val suppliment1_1 : ArrayList<Suppliment> = jsonToObject( Unirest.get(origin + "/api/suppliments/1").asString().body.toString())
            assertEquals(total_suppliments, suppliment1_1.size)
        }

        @Test
        fun `adding and updating a suppliment for an existing user`() {
            val userId = 1
            val suppliments : ArrayList<Suppliment> = jsonToObject( Unirest.get(origin + "/api/suppliments/$userId").asString().body.toString())
            val suppliment = suppliments[0]
            val id = suppliment.id
            val oldvitamin_d = suppliment.vitamin_d
            val newVitamin_d = oldvitamin_d+50
            updateSuppliment(id, vitamin_d = newVitamin_d, vitamin_c = suppliment.vitamin_c, iron = suppliment.iron, calcium = suppliment.calcium, userId = userId)
            val newSuppliments : ArrayList<Suppliment> = jsonToObject( Unirest.get(origin + "/api/suppliments/$userId").asString().body.toString())
            assertEquals(newSuppliments[0].vitamin_d, newVitamin_d)
            assertNotEquals(newSuppliments[0].vitamin_d,oldvitamin_d)
        }


    }

    @Nested
    inner class SportAPI{

        @Test
        fun `get a sports by  using a userId that exists in the database returns 200 `() {
            val response = Unirest.get(origin + "/api/sports/1").asString()
            assertEquals(200, response.status)

        }

        @Test
        fun `get sports by user id when user does not exist returns empty value sports and 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/sports/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        //helper function to add a test sport to the database
        private fun addSport (userId: Int, sports_name: String, playing_hours: Double): HttpResponse<JsonNode> {
            return Unirest.post(origin + "/api/sports")
                .body("{\"userId\":$userId, \"sports_name\":\"$sports_name\", \"playing_hours\":$playing_hours}")
                .asJson()
        }

        //helper function to update a test sport to the database
        private fun updateSport (id: Int, userId: Int, sports_name: String, playing_hours: Double): HttpResponse<JsonNode> {
            return Unirest.put(origin + "/api/sports")
                .body("{\"id\":$id, \"userId\":$userId, \"sports_name\":\"$sports_name\", \"playing_hours\":$playing_hours}")
                .asJson()
        }

        //helper function to delete a sport user from the database
        private fun deleteSport (id: Int): HttpResponse<String> {
            return Unirest.delete(origin + "/api/sports/$id").asString()
        }


        @Test
        fun `adding and deleting a sport for an existing user`() {
            val sport1 : ArrayList<Sport> = jsonToObject( Unirest.get(origin + "/api/sports/1").asString().body.toString())
            var total_sports = sport1.size
            deleteSport(1)
            val sport1_2 : ArrayList<Sport> = jsonToObject( Unirest.get(origin + "/api/sports/1").asString().body.toString())
            assertEquals(total_sports-1, sport1_2.size)
            addSport(1, "cricket", 0.0)
            val sport1_1 : ArrayList<Sport> = jsonToObject( Unirest.get(origin + "/api/sports/1").asString().body.toString())
            assertEquals(total_sports, sport1_1.size)
        }

        @Test
        fun `adding and updating a sport for an existing user`() {
            val userId = 1
            val sports : ArrayList<Sport> = jsonToObject( Unirest.get(origin + "/api/sports/$userId").asString().body.toString())
            val sport = sports[0]
            val id = sport.id
            val oldsports_name = sport.sports_name
            val newSports_name = "new sports"
            updateSport(id, sports_name = newSports_name, playing_hours = sport.playing_hours, userId = userId)
            val newSports : ArrayList<Sport> = jsonToObject( Unirest.get(origin + "/api/sports/$userId").asString().body.toString())
            assertEquals(newSports[0].sports_name, newSports_name)
            assertNotEquals(newSports[0].sports_name,oldsports_name)
        }


    }



    // Generic function to convert JSON string to an object
    inline fun <reified T> jsonToObject(json: String): T {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(json)
    }





}