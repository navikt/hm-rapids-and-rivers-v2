package no.nav.helse.rapids_rivers.testsupport


import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.isMissingOrNull
import tools.jackson.databind.JsonNode

import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper

class TestRapid : RapidsConnection() {
    private companion object {
        private val objectMapper =
            JsonMapper.builderWithJackson2Defaults().disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS).build()
    }

    private val messages = mutableListOf<Pair<String?, String>>()
    val inspektør get() = RapidInspector(messages.toList())

    fun reset() {
        messages.clear()
    }

    fun sendTestMessage(message: String) {
        notifyMessage(message, this)
    }

    override fun publish(message: String) {
        messages.add(null to message)
    }

    override fun publish(key: String, message: String) {
        messages.add(key to message)
    }

    override fun publishWithTimeout(message: String, timeoutInSec: Long) {
        TODO("Not yet implemented")
    }

    override fun publishWithTimeout(key: String, message: String, timeoutInSec: Long) {
        messages.add(key to message)
    }

    override fun rapidName(): String {
        return "testRapid"
    }

    override fun start() {}
    override fun stop() {}

    class RapidInspector(private val messages: List<Pair<String?, String>>) {
        private val jsonMessages = mutableMapOf<Int, JsonNode>()
        val size get() = messages.size

        fun key(index: Int) = messages[index].first
        fun message(index: Int) = jsonMessages.getOrPut(index) { objectMapper.readTree(messages[index].second) }
        fun field(index: Int, field: String) = requireNotNull(message(index).path(field).takeUnless(JsonNode::isMissingOrNull)) {
            "Message does not contain field '$field'"
        }
    }
}
