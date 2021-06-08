package domain.service

import domain.model.AddFlightResponse
import domain.model.DeleteFlightResponse
import domain.model.FlightAlreadyExists
import domain.model.FlightDoesMatchError
import domain.model.FlightEvent
import domain.model.FlightNotFoundError
import domain.model.QueryFlightResponse
import domain.model.UpdateFlightResponse
import java.time.LocalDateTime

class FlightControlService(private val flightDatabase: MutableMap<String, FlightEvent> = mutableMapOf()) {

    fun addFlight(flightToAdd: FlightEvent): AddFlightResponse {
        if (flightDatabase[flightToAdd.planeId] != null) throw FlightAlreadyExists

        flightDatabase[flightToAdd.planeId] = flightToAdd
        return AddFlightResponse(flightDatabase.allFlights())
    }

    fun updateFlight(flightToUpdate: FlightEvent): UpdateFlightResponse {
        flightDatabase[flightToUpdate.planeId] ?: throw FlightNotFoundError

        flightDatabase[flightToUpdate.planeId] = flightToUpdate
        return UpdateFlightResponse(flightDatabase.allFlights())
    }

    fun deleteFlight(flightToDelete: FlightEvent): DeleteFlightResponse {
        val existingFlight = flightDatabase[flightToDelete.planeId] ?: throw FlightNotFoundError

        if (existingFlight != flightToDelete) throw FlightDoesMatchError

        flightDatabase.remove(flightToDelete.planeId)
        return DeleteFlightResponse(flightDatabase.allFlights())
    }

    fun queryFlights(eventTime: LocalDateTime): QueryFlightResponse {
        val matchedFlights = flightDatabase
            .allFlights()
            .filter { it.eventTime == eventTime }
        return QueryFlightResponse(matchedFlights)
    }

    private fun MutableMap<String, FlightEvent>.allFlights() = this.values.toList()
}