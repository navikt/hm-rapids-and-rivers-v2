package no.nav.helse.rapids_rivers

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.micrometer.core.instrument.binder.MeterBinder
import org.slf4j.LoggerFactory

class RiverMetrics: MeterBinder {

    var meterRegistry: MeterRegistry = DefaultMeterRegistry.Default

    companion object {
        private val LOG = LoggerFactory.getLogger(RiverMetrics::class.java)
    }
    override fun bindTo(registry: MeterRegistry) {
        LOG.debug("Binding to meter registry {}", registry)
        this.meterRegistry = registry
    }

    fun messageCounter(rapidName:String, riverName: String, status: String) {
        LOG.debug("Incrementing message counter for rapid {} and river {} with status {}", rapidName, riverName, status)
        Counter.builder("message_counter").tags("rapid",rapidName, "river", riverName, "status", status)
            .register(meterRegistry)
            .increment()
    }

    fun timer(rapidName: String, riverName: String, eventName: String, runnable: Runnable) {
        LOG.debug("Recording timer for rapid {} and river {} and event {}", rapidName, riverName, eventName)
        Timer.builder("on_packet_seconds")
            .tags("rapid", rapidName, "river", riverName, "event_name", eventName)
            .register(meterRegistry)
            .record(runnable)
    }
}