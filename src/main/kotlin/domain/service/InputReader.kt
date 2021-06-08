package domain.service

import domain.model.FlightControlOption
import domain.model.NoInputError
import domain.model.UnknownInputError

class InputReader {

    fun readOption(): FlightControlOption {
        val option = readLine() ?: throw NoInputError
        return FlightControlOption.from(option) ?: throw UnknownInputError
    }

    fun readRequestData() = readLine() ?: throw NoInputError
}