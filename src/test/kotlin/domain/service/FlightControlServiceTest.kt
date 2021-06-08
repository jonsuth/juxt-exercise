package domain.service

import domain.model.AddFlightResponse
import domain.model.DeleteFlightResponse
import domain.model.FlightAlreadyExists
import domain.model.FlightDoesMatchError
import domain.model.FlightEvent
import domain.model.FlightNotFoundError
import domain.model.QueryFlightResponse
import domain.model.UpdateFlightResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.elementAt
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import java.time.LocalDateTime

internal class FlightControlServiceTest {

    private val flightDatabase = mutableMapOf<String, FlightEvent>()
    private val flightControlService = FlightControlService(flightDatabase)

    @BeforeEach
    fun clearFlightDatabase() {
        flightDatabase.clear()
    }

    @Nested
    inner class AddFlight {
        @Test
        fun `should add flight`() {
            //given
            val request = FlightEvent("F123", "747", "London", "Paris", "Re-Fuel", LocalDateTime.parse("2021-03-29T12:00:00"), 250)

            //when
            val response = flightControlService.addFlight(request)

            //then
            expectThat(response).isA<AddFlightResponse>() and {
                get { flights } hasSize 1 and {
                    elementAt(0) and {
                        get { planeId } isEqualTo "F123"
                        get { planeModel } isEqualTo "747"
                        get { origin } isEqualTo "London"
                        get { destination } isEqualTo "Paris"
                        get { eventType } isEqualTo "Re-Fuel"
                        get { eventTime } isEqualTo LocalDateTime.parse("2021-03-29T12:00:00")
                        get { fuelDelta } isEqualTo 250
                    }
                }
            }
        }

        @Test
        fun `should throw FlightAlreadyExists when adding a flight that already exists in the database`() {
            //given
            flightDatabase["F456"] = FlightEvent("F456", "747", "London", "Paris", "Re-Fuel", LocalDateTime.parse("2021-03-29T12:00:00"), 250)
            val request = FlightEvent("F456", "747", "London", "Paris", "Re-Fuel", LocalDateTime.parse("2021-03-29T12:00:00"), 250)

            //when and then
            expectThrows<FlightAlreadyExists> { flightControlService.addFlight(request) }
        }
    }

    @Nested
    inner class UpdateFlight {
        @Test
        fun `should throw FlightNotFoundError when updating a flight that does not exist`() {
            //given
            flightDatabase["F456"] = FlightEvent("F456", "747", "London", "Paris", "Re-Fuel", LocalDateTime.parse("2021-03-29T12:00:00"), 250)
            val request = FlightEvent("F123", "747", "London", "Paris", "In-Flight", LocalDateTime.parse("2021-03-29T12:00:00"), 250)

            //when and then
            expectThrows<FlightNotFoundError> { flightControlService.updateFlight(request) }
        }

        @Test
        fun `should update flight`() {
            //given
            flightDatabase["F123"] = FlightEvent("F123", "747", "London", "Paris", "Re-Fuel", LocalDateTime.parse("2021-03-29T12:00:00"), 250)
            val request = FlightEvent("F123", "747", "London", "Paris", "In-Flight", LocalDateTime.parse("2021-03-29T12:00:00"), 250)

            //when
            val response = flightControlService.updateFlight(request)

            //then
            expectThat(response).isA<UpdateFlightResponse>() and {
                get { flights } hasSize 1 and {
                    elementAt(0) and {
                        get { planeId } isEqualTo "F123"
                        get { planeModel } isEqualTo "747"
                        get { origin } isEqualTo "London"
                        get { destination } isEqualTo "Paris"
                        get { eventType } isEqualTo "In-Flight"
                        get { eventTime } isEqualTo LocalDateTime.parse("2021-03-29T12:00:00")
                        get { fuelDelta } isEqualTo 250
                    }
                }
            }
        }
    }

    @Nested
    inner class DeleteFlight {
        @Test
        fun `should throw FlightNotFoundError when deleting a flight that does not exist`() {
            //given
            flightDatabase["F123"] = FlightEvent("F123", "747", "London", "Paris", "Re-Fuel", LocalDateTime.parse("2021-03-29T12:00:00"), 250)
            flightDatabase["F456"] = FlightEvent("F456", "747", "New York", "London", "In-Flight", LocalDateTime.parse("2021-03-29T12:00:00"), 250)
            flightDatabase["F789"] = FlightEvent("F789", "747", "Toronto", "Paris", "Take-Off", LocalDateTime.parse("2021-03-29T12:00:00"), 250)

            val request = FlightEvent("F567", "747", "New York", "London", "Take-Off", LocalDateTime.parse("2021-03-29T12:00:00"), 250)

            //when and then
            expectThrows<FlightNotFoundError> { flightControlService.deleteFlight(request) }
        }

        @Test
        fun `should throw FlightDoesMatchError when deleting a flight that does not match the existing flight`() {
            //given
            flightDatabase["F123"] = FlightEvent("F123", "747", "London", "Paris", "Re-Fuel", LocalDateTime.parse("2021-03-29T12:00:00"), 250)
            flightDatabase["F456"] = FlightEvent("F456", "747", "New York", "London", "In-Flight", LocalDateTime.parse("2021-03-29T12:00:00"), 250)
            flightDatabase["F789"] = FlightEvent("F789", "747", "Toronto", "Paris", "Take-Off", LocalDateTime.parse("2021-03-29T12:00:00"), 250)

            val request = FlightEvent("F456", "747", "New York", "London", "Take-Off", LocalDateTime.parse("2021-03-29T12:00:00"), 250)

            //when and then
            expectThrows<FlightDoesMatchError> { flightControlService.deleteFlight(request) }
        }

        @Test
        fun `should delete flight`() {
            //given
            flightDatabase["F123"] = FlightEvent("F123", "747", "London", "Paris", "Re-Fuel", LocalDateTime.parse("2021-03-29T12:00:00"), 250)
            flightDatabase["F456"] = FlightEvent("F456", "747", "New York", "London", "In-Flight", LocalDateTime.parse("2021-03-29T12:00:00"), 250)
            flightDatabase["F789"] = FlightEvent("F789", "747", "Toronto", "Paris", "Take-Off", LocalDateTime.parse("2021-03-29T12:00:00"), 250)

            val request = FlightEvent("F456", "747", "New York", "London", "In-Flight", LocalDateTime.parse("2021-03-29T12:00:00"), 250)

            //when
            val response = flightControlService.deleteFlight(request)

            //then
            expectThat(response).isA<DeleteFlightResponse>() and {
                get { flights } hasSize 2 and {
                    elementAt(0) and {
                        get { planeId } isEqualTo "F123"
                        get { planeModel } isEqualTo "747"
                        get { origin } isEqualTo "London"
                        get { destination } isEqualTo "Paris"
                        get { eventType } isEqualTo "Re-Fuel"
                        get { eventTime } isEqualTo LocalDateTime.parse("2021-03-29T12:00:00")
                        get { fuelDelta } isEqualTo 250
                    }
                    elementAt(1) and {
                        get { planeId } isEqualTo "F789"
                        get { planeModel } isEqualTo "747"
                        get { origin } isEqualTo "Toronto"
                        get { destination } isEqualTo "Paris"
                        get { eventType } isEqualTo "Take-Off"
                        get { eventTime } isEqualTo LocalDateTime.parse("2021-03-29T12:00:00")
                        get { fuelDelta } isEqualTo 250
                    }
                }
            }
        }
    }

    @Nested
    inner class QueryFlight {
        @Test
        fun `should return not flights when querying and empty flight database flights`() {
            //given
            val request = LocalDateTime.parse("2021-03-29T12:00:00")

            //when
            val response = flightControlService.queryFlights(request)

            //then
            expectThat(response).isA<QueryFlightResponse>() and {
                get { flights }.isEmpty()
            }
        }

        @Test
        fun `should query flights`() {
            //given
            flightDatabase["F123"] = FlightEvent("F123", "747", "London", "Paris", "Re-Fuel", LocalDateTime.parse("2021-12-29T12:00:00"), 250)
            flightDatabase["F234"] = FlightEvent("F234", "747", "New York", "London", "In-Flight", LocalDateTime.parse("2021-03-29T12:00:00"), 250)
            flightDatabase["F345"] = FlightEvent("F345", "747", "Toronto", "Paris", "Take-Off", LocalDateTime.parse("2021-04-29T12:00:00"), 250)
            flightDatabase["F456"] = FlightEvent("F456", "747", "Toronto", "Paris", "Take-Off", LocalDateTime.parse("2021-03-29T12:00:00"), 250)
            flightDatabase["F567"] = FlightEvent("F567", "747", "Toronto", "Cairo", "Take-Off", LocalDateTime.parse("2021-10-29T12:00:00"), 250)

            val request = LocalDateTime.parse("2021-03-29T12:00:00")

            //when
            val response = flightControlService.queryFlights(request)

            //then
            expectThat(response).isA<QueryFlightResponse>() and {
                get { flights } hasSize 2 and {
                    elementAt(0) and {
                        get { planeId } isEqualTo "F234"
                        get { planeModel } isEqualTo "747"
                        get { origin } isEqualTo "New York"
                        get { destination } isEqualTo "London"
                        get { eventType } isEqualTo "In-Flight"
                        get { eventTime } isEqualTo LocalDateTime.parse("2021-03-29T12:00:00")
                        get { fuelDelta } isEqualTo 250
                    }
                    elementAt(1) and {
                        get { planeId } isEqualTo "F456"
                        get { planeModel } isEqualTo "747"
                        get { origin } isEqualTo "Toronto"
                        get { destination } isEqualTo "Paris"
                        get { eventType } isEqualTo "Take-Off"
                        get { eventTime } isEqualTo LocalDateTime.parse("2021-03-29T12:00:00")
                        get { fuelDelta } isEqualTo 250
                    }
                }
            }
        }
    }
}