package no.nav.hm.rapids_rivers.micronaut

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton
import no.nav.helse.rapids_rivers.KafkaRapid
import org.apache.kafka.common.KafkaException
import org.slf4j.LoggerFactory
import java.util.*

@Singleton
@Requires(bean = KafkaRapid::class)
class KafkaRapidService(private val kafkaRapid: KafkaRapid, private val objectMapper: ObjectMapper) {

    companion object {
        private val LOG = LoggerFactory.getLogger(KafkaRapidService::class.java)
    }

    fun <T : Any> pushToRapid(key: String?, eventName: String?, payload: T) {
        val eventId =  UUID.randomUUID()
        LOG.debug("push to rapid key: $key eventId: $eventId eventName: $eventName")
        produceEvent(key, mapOf("eventId" to eventId, "eventName" to eventName, "payload" to payload))
    }

    private fun <T> produceEvent(key: String?, event: T) {
        try {
            val message = objectMapper.writeValueAsString(event)
            if (key != null) kafkaRapid.publishWithTimeout(key, message, 10)
            else kafkaRapid.publishWithTimeout(message, 10)
        } catch (e: Exception) {
            LOG.error("We got error while sending to kafka", e)
            throw KafkaException("Error while sending to kafka", e)
        }
    }
}