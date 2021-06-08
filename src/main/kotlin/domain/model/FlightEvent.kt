package domain.model

import java.time.LocalDateTime

data class FlightEvent(
    val planeId: String,
    val planeModel: String,
    val origin: String,
    val destination: String,
    val eventType: String,
    val eventTime: LocalDateTime,
    val fuelDelta: Int
) {
    override fun toString(): String {
        return "$planeId $eventType $fuelDelta"
    }
}
