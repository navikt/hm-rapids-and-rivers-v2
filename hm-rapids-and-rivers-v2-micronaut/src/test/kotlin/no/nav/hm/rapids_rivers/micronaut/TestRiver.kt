package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.context.annotation.Context
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.MessageContext
import no.nav.helse.rapids_rivers.River

@Context
class TestRiver(river: RiverHead): River.PacketListener {

    init {
        river
            .validate { it.demandValue("key", "value")}
            .validate { it.demandKey("key")}
            .register(this)
    }

    override fun onPacket(packet: JsonMessage, context: MessageContext) {
        TODO("Not yet implemented")
    }
}