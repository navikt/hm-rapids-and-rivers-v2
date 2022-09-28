package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.Async
import jakarta.inject.Singleton
import no.nav.helse.rapids_rivers.KafkaRapid
import org.slf4j.LoggerFactory

@Singleton
open class RapidStartupEventListener(private val rapid: KafkaRapid): ApplicationEventListener<ServerStartupEvent> {

    companion object {
        private val LOG = LoggerFactory.getLogger(RapidStartupEventListener::class.java)
    }

    @Async(TaskExecutors.MESSAGE_CONSUMER)
    override fun onApplicationEvent(event: ServerStartupEvent) {
        LOG.info("Startup rapid")
        rapid.start()
    }

}