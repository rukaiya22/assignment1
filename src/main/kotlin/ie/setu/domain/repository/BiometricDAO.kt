package ie.setu.domain.repository

import ie.setu.domain.Biometric
import ie.setu.domain.db.Biometrics
import ie.setu.utils.mapToBiometric
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class BiometricDAO {
    fun getAll() : ArrayList<Biometric>{
        val biometricList: ArrayList<Biometric> = arrayListOf()
        transaction {
            Biometrics.selectAll().map {
                biometricList.add(mapToBiometric(it)) }
        }
        return biometricList
    }

    fun save(biometric: Biometric) {
        transaction {
            Biometrics.insert {
                it[userId] = biometric.userId
                it[bp_systolic] = biometric.bp_systolic
                it[bp_diastolic] = biometric.bp_diastolic
                it[blood_sugar] = biometric.blood_sugar
                it[cholesterol] = biometric.cholesterol
            }
        }
    }

    fun update(id: Int, biometric: Biometric){
        transaction {
            Biometrics.update({ Biometrics.id eq id }) {
                it[bp_systolic] = biometric.bp_systolic
                it[bp_diastolic] = biometric.bp_diastolic
                it[blood_sugar] = biometric.blood_sugar
                it[cholesterol] = biometric.cholesterol
            }
        }
    }

    fun delete(id: Int) {
        transaction {
            Biometrics.deleteWhere { Biometrics.id eq id }
        }
    }

    fun findByUserId(userId: Int): ArrayList<Biometric>{
        val biometricList: ArrayList<Biometric> = arrayListOf()
        transaction {
            Biometrics.selectAll().where { Biometrics.userId eq userId }.map {
                biometricList.add(mapToBiometric(it)) }
        }
        return biometricList
    }

}