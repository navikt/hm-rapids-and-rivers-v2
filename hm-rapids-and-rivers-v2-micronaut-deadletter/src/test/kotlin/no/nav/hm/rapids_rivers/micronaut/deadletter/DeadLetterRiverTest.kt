package no.nav.hm.rapids_rivers.micronaut.deadletter

import io.micronaut.context.annotation.Context

import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.MessageContext
import no.nav.helse.rapids_rivers.River
import no.nav.hm.rapids_rivers.micronaut.RiverHead

@Context
open class DeadLetterRiverTest(river: RiverHead): River.PacketListener {

    init {
        river
            .validate { it.demandValue("key", "value")}
            .validate { it.demandKey("key")}
            .register(this)
    }

    @DeadLetterSupport
    override open fun onPacket(packet: JsonMessage, context: MessageContext) {

        throw RuntimeException("Not yet implemented")
    }


}