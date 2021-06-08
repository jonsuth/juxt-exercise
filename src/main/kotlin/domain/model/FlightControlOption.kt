package domain.model

enum class FlightControlOption(val option: String) {
    AddFlight("1"),
    UpdateFlight("2"),
    DeleteFlight("3"),
    QueryFlight("4");

    companion object {
        fun from(option: String): FlightControlOption? {
            return values().find { it.option == option }
        }
    }
}