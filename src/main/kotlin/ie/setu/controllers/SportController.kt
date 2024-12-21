package ie.setu.controllers

import ie.setu.domain.Sport
import ie.setu.domain.repository.SportDAO



class SportController {
    private val sportDAO = SportDAO()

    fun getAllSports(): ArrayList<Sport> {
        return sportDAO.getAll()
    }

    fun getSportsByUserId(userId: Int): ArrayList<Sport> {
        return sportDAO.findByUserId(userId)
    }

    fun createSport(sport: Sport): Int {
        sportDAO.save(sport)
        return 201
    }

    fun updateSport(sport: Sport): Int {
        sportDAO.update(sport.id, sport)
        return 201
    }

    fun deletedSport(sportId: Int): Int {
        sportDAO.delete(sportId)
        return 204
    }

}