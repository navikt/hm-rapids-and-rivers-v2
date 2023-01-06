package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.event.ApplicationShutdownEvent
import jakarta.inject.Singleton
import no.nav.helse.rapids_rivers.KafkaRapid
import org.slf4j.LoggerFactory


@Singleton
@Requires(bean = KafkaRapid::class)
class RapidShutDownEventListener(private val rapid: KafkaRapid): ApplicationEventListener<ApplicationShutdownEvent> {
    companion object {
        private val LOG = LoggerFactory.getLogger(RapidShutDownEventListener::class.java)
    }

    override fun onApplicationEvent(event: ApplicationShutdownEvent) {
        LOG.info("received $event, will shutdown rapid")
        rapid.stop()
    }

}