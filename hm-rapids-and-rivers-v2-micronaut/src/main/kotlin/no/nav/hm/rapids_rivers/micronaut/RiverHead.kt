package no.nav.hm.rapids_rivers.micronaut


import io.micrometer.core.instrument.MeterRegistry
import io.micronaut.context.annotation.Prototype
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.River
import no.nav.helse.rapids_rivers.RiverMetrics
import org.slf4j.LoggerFactory

@Prototype
class RiverHead(rapidsConnection: RapidsConnection, riverMetrics: RiverMetrics) {

    private val river = River(rapidsConnection, riverMetrics)

    companion object {
        private val LOG = LoggerFactory.getLogger(River::class.java)
    }

    fun validate(validation: River.PacketValidation): RiverHead {
        river.validate(validation)
        return this
    }

    fun register(packetListener: River.PacketListener) {
        LOG.info("Registering ${packetListener.javaClass.simpleName}")
        river.register(packetListener)
    }

}