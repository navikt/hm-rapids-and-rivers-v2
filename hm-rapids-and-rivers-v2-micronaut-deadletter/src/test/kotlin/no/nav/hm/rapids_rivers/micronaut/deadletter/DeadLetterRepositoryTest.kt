package no.nav.hm.rapids_rivers.micronaut.deadletter

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import java.util.UUID
import kotlinx.coroutines.runBlocking
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.MessageProblems
import org.junit.jupiter.api.Test

@MicronautTest
class DeadLetterRepositoryTest(private val deadLetterRepository: DeadLetterRepository) {

    @Test
    fun testDeadLetterRepository() {
        runBlocking {
            val saved = deadLetterRepository.save(
                DeadLetter(
                    eventId = UUID.randomUUID().toString(),
                    eventName = "test",
                    json = """{"test": "test"}""",
                    error = "test",
                    topic = "test",
                    riverName = "test"
                )
            )
        }
    }
}