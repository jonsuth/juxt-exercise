package domain.model


object NoInputError : Exception("No input detected")
object UnknownInputError : Exception("Unknown input detected")
object MissingFlightFieldsError : Exception("Missing the mandatory flight fields required to add this flight event")
object IncorrectEventTimeFormat : Exception("Event time is in the incorrect format. Required format: YYYY-MM-DD:HH:MM:SS")

object FlightAlreadyExists : Exception("A flight with this ID already exists in the database")
object FlightNotFoundError : Exception("A flight with this ID does not exist in the database")
object FlightDoesMatchError : Exception("The flight with this ID does not match the entry found in the database")
