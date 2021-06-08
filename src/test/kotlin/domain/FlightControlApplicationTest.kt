package domain

import domain.model.AddFlightResponse
import domain.model.DeleteFlightResponse
import domain.model.FlightControlOption.AddFlight
import domain.model.FlightControlOption.DeleteFlight
import domain.model.FlightControlOption.QueryFlight
import domain.model.FlightControlOption.UpdateFlight
import domain.model.QueryFlightResponse
import domain.model.UpdateFlightResponse
import domain.service.FlightControlService
import domain.service.InputParser
import domain.service.InputReader
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test

internal class FlightControlApplicationTest {

    private val flightControlService = mockk<FlightControlService>()
    private val inputParser = spyk<InputParser>()
    private val inputReader = mockk<InputReader>()
    private val flightControlApplication = FlightControlApplication(flightControlService, inputParser, inputReader)

    @Test
    fun `should call add flight service`() {
        //given
        every { flightControlService.addFlight(any()) } returns AddFlightResponse(emptyList())
        every { inputReader.readOption() } returns AddFlight
        every { inputReader.readRequestData() } returns "F551 747 PARIS LONDON Land 2021-03-29T12:00:00 -220"

        // when
        flightControlApplication.processRequest()

        //then
        verifyOrder {
            inputParser.parseFlightEvent(any())
            flightControlService.addFlight(any())
        }
    }

    @Test
    fun `should call update flight service`() {
        //given
        every { flightControlService.updateFlight(any()) } returns UpdateFlightResponse(emptyList())
        every { inputReader.readOption() } returns UpdateFlight
        every { inputReader.readRequestData() } returns "F551 747 PARIS LONDON Land 2021-03-29T12:00:00 -220"

        // when
        flightControlApplication.processRequest()

        //then
        verifyOrder {
            inputParser.parseFlightEvent(any())
            flightControlService.updateFlight(any())
        }
    }

    @Test
    fun `should call delete flight service`() {
        //given
        every { flightControlService.deleteFlight(any()) } returns DeleteFlightResponse(emptyList())
        every { inputReader.readOption() } returns DeleteFlight
        every { inputReader.readRequestData() } returns "F551 747 PARIS LONDON Land 2021-03-29T12:00:00 -220"

        // when
        flightControlApplication.processRequest()

        //then
        verifyOrder {
            inputParser.parseFlightEvent(any())
            flightControlService.deleteFlight(any())
        }
    }

    @Test
    fun `should call query flight service`() {
        //given
        every { flightControlService.queryFlights(any()) } returns QueryFlightResponse(emptyList())
        every { inputReader.readOption() } returns QueryFlight
        every { inputReader.readRequestData() } returns "2021-03-29T12:00:00"

        // when
        flightControlApplication.processRequest()

        //then
        verifyOrder {
            inputParser.parseEventTime(any())
            flightControlService.queryFlights(any())
        }
    }
}