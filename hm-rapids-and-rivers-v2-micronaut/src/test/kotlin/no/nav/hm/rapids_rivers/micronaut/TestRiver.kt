package no.nav.hm.rapids_rivers.micronaut

import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.MessageContext
import no.nav.helse.rapids_rivers.RapidsConnection

@RiverBean
class TestRiver(rapidsConnection: RapidsConnection) : River(rapidsConnection) {

    init {
        validate{ it.demandValue("key", "value")}
        validate{ it.requireKey("TestKey")}
    }

    override fun onPacket(packet: JsonMessage, context: MessageContext) {
        TODO("Not yet implemented")
    }
}