package domain

import domain.model.FlightControlOption
import domain.model.FlightControlOption.AddFlight
import domain.model.FlightControlOption.DeleteFlight
import domain.model.FlightControlOption.QueryFlight
import domain.model.FlightControlOption.UpdateFlight
import domain.model.FlightControlResponse
import domain.service.FlightControlService
import domain.service.InputParser
import domain.service.InputReader

class FlightControlApplication(
    private val flightControlService: FlightControlService = FlightControlService(),
    private val inputParser: InputParser = InputParser(),
    private val inputReader: InputReader = InputReader()
) {

    fun processRequest() {
        println()
        println("Flight Control")
        println("----------------")
        println("1. Add flight")
        println("2. Update flight")
        println("3. Delete flight")
        println("4. Query flights")
        handlingException {
            val option = inputReader.readOption()
            println("Enter request data for option: ${option.name}")
            val request = inputReader.readRequestData()
            val response = handleOption(option, request)
            response.print()
        }
    }

    private fun handleOption(option: FlightControlOption, request: String): FlightControlResponse {
        return when (option) {
            AddFlight -> flightControlService.addFlight(inputParser.parseFlightEvent(request))
            UpdateFlight -> flightControlService.updateFlight(inputParser.parseFlightEvent(request))
            DeleteFlight -> flightControlService.deleteFlight(inputParser.parseFlightEvent(request))
            QueryFlight -> flightControlService.queryFlights(inputParser.parseEventTime(request))
        }
    }

    private fun handlingException(function: () -> Unit) {
        try {
            function()
        } catch (exception: Exception) {
            println(exception.message)
        }
    }
}