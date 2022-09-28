package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import no.nav.helse.rapids_rivers.KafkaRapid
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
@Property(name = "kafka.brokers", value = "localhost:9092")
class RapidApplicationTest(private val kafkaProperties: KafkaProperties,
                           private val kafkaRapid: KafkaRapid,
                           private val testRiver: River) {

    @Test
    fun `Rapid should be created`() {
        Assertions.assertNotNull(kafkaProperties.brokers)
        Assertions.assertEquals(kafkaProperties.brokers, "localhost:9092")
        Assertions.assertNotNull(kafkaRapid)
    }

    @Test
    fun `River is running`() {
         Assertions.assertNotNull(testRiver)
    }

}