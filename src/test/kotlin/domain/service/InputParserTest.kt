package domain.service

import domain.model.IncorrectEventTimeFormat
import domain.model.MissingFlightFieldsError
import domain.model.NoInputError
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import java.time.LocalDateTime

internal class InputParserTest {

    private val inputParser = InputParser()

    @Test
    fun `should throw NoInputError when raw input is null`() {
        //given
        val rawInput = null

        //then
        expectThrows<NoInputError> { inputParser.parseFlightEvent(rawInput) }
    }

    @Test
    fun `should throw MissingFieldsError when raw input is missing fields`() {
        //given
        val rawInput = "F551 747 PARIS LONDON Land 2021-03-29T12:00:00"

        //then
        expectThrows<MissingFlightFieldsError> { inputParser.parseFlightEvent(rawInput) }
    }

    @Test
    fun `should throw IncorrectEventTimeFormat when event time does not match format`() {
        //given
        val rawInput = "F551 747 PARIS LONDON Land 2021-3-29T12:00:00 -120"

        //then
        expectThrows<IncorrectEventTimeFormat> { inputParser.parseFlightEvent(rawInput) }
    }

    @Test
    fun `should parse flight event`() {
        //given
        val rawInput = "F551 747 PARIS LONDON Land 2021-03-29T12:00:00 -120"

        //when
        val flightEvent = inputParser.parseFlightEvent(rawInput)

        //then
        expectThat(flightEvent) {
            get { planeId } isEqualTo "F551"
            get { planeModel } isEqualTo "747"
            get { origin } isEqualTo "PARIS"
            get { destination } isEqualTo "LONDON"
            get { eventType } isEqualTo "Land"
            get { eventTime } isEqualTo LocalDateTime.of(2021, 3, 29, 12, 0, 0)
            get { fuelDelta } isEqualTo -120
        }
    }
}