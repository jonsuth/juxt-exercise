package domain.service

import domain.model.FlightEvent
import domain.model.IncorrectEventTimeFormat
import domain.model.MissingFlightFieldsError
import domain.model.NoInputError
import java.time.LocalDateTime

class InputParser {

    fun parseFlightEvent(rawInput: String?): FlightEvent {
        if (rawInput == null) throw NoInputError

        val fields = rawInput.split(" ")

        if (fields.size != 7) throw MissingFlightFieldsError

        return FlightEvent(
            planeId = fields[0],
            planeModel = fields[1],
            origin = fields[2],
            destination = fields[3],
            eventType = fields[4],
            eventTime = parseEventTime(fields[5]),
            fuelDelta = fields[6].toInt()
        )
    }

    fun parseEventTime(rawInput: String?): LocalDateTime {
        if (rawInput == null) throw NoInputError

        return try {
            LocalDateTime.parse(rawInput)
        } catch (e: Exception) {
            throw IncorrectEventTimeFormat
        }
    }
}