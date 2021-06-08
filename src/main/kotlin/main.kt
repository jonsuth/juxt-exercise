import domain.FlightControlApplication

fun main(args: Array<String>) {
    val application = FlightControlApplication()
    while (true) {
        application.processRequest()
    }
}
