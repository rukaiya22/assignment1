package ie.setu.controllers

import ie.setu.domain.Tracker
import ie.setu.domain.repository.TrackerDAO



class TrackerController {
    private val trackerDAO = TrackerDAO()

    fun getAllTrackers(): ArrayList<Tracker> {
        return trackerDAO.getAll()
    }

    fun getTrackersByUserId(userId: Int): ArrayList<Tracker> {
        return trackerDAO.findByUserId(userId)
    }

    fun createTracker(tracker: Tracker): Int {
        trackerDAO.save(tracker)
        return 201
    }

    fun updateTracker(tracker: Tracker): Int {
        trackerDAO.update(tracker.id, tracker)
        return 201
    }

    fun deletedTracker(trackerId: Int): Int {
        trackerDAO.delete(trackerId)
        return 204
    }

}