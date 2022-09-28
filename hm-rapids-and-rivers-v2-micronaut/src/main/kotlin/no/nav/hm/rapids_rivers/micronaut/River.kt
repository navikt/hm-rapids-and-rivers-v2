package no.nav.hm.rapids_rivers.micronaut


import no.nav.helse.rapids_rivers.MessageContext
import no.nav.helse.rapids_rivers.MessageProblems
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.River
import org.slf4j.LoggerFactory

abstract class River(rapidsConnection: RapidsConnection): River.PacketListener{

    companion object {
        private val LOG = LoggerFactory.getLogger(River::class.java)
        private val securedLog = LoggerFactory.getLogger("tjenestekall")
    }

    private val river: River

    init {
        river = River(rapidsConnection)
    }

    fun validate(validation: River.PacketValidation) = river.validate(validation)


    fun register() {
        LOG.info("Registering ${this.javaClass.simpleName}")
        river.register(this)
    }

    override fun onError(problems: MessageProblems, context: MessageContext) {
        securedLog.info("River required keys had problems in parsing message from rapid: ${problems.toExtendedReport()}")
        throw RuntimeException("River required keys had problems in parsing message from rapid, see Kibana index tjenestekall-* (sikkerlogg) for details")
    }
}