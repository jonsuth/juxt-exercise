package domain.model

open class FlightControlResponse(private val message: String, val flights: List<FlightEvent>) {
    fun print() {
        println(this.message)
        this.flights.forEach {
            println(it.toString())
        }
    }
}

class AddFlightResponse(flights: List<FlightEvent>) : FlightControlResponse("Flight successfully added:", flights)
class UpdateFlightResponse(flights: List<FlightEvent>) : FlightControlResponse("Flight successfully updated:", flights)
class DeleteFlightResponse(flights: List<FlightEvent>) : FlightControlResponse("Flight successfully deleted:", flights)
class QueryFlightResponse(flights: List<FlightEvent>) : FlightControlResponse("Flights queried via event time:", flights)