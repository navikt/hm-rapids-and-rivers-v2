package no.nav.hm.rapids_rivers.micronaut


import io.micronaut.context.annotation.Prototype
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.River
import org.slf4j.LoggerFactory

@Prototype
class RiverHead(rapidsConnection: RapidsConnection) {

    private val river = River(rapidsConnection)

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