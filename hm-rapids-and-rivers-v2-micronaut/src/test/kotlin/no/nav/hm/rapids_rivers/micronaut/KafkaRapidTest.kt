package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import no.nav.helse.rapids_rivers.KafkaRapid
import org.junit.jupiter.api.Test

@MicronautTest
class KafkaRapidTest(private val kafkaRapid: KafkaRapid) {

    //@Test
    fun publishToKafkaTest() {
        kafkaRapid.publishWithTimeout("Dette er en test melding", 10)
    }
}