package no.nav.helse.rapids_rivers

import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder

class KafkaRapidMetrics(private val rapid: KafkaRapid): MeterBinder {

    override fun bindTo(registry: MeterRegistry) {
        Gauge.builder("rapids_rivers_consumer_active", this) {consumerActive()}.register(registry)
        Gauge.builder("rapids_rivers_producer_active", this) {producerActive()}.register(registry)
    }

    private fun producerActive():Double {
        return if(rapid.isProducerClosed()) 0.0 else 1.0
    }

    private fun consumerActive(): Double {
        return if (rapid.isConsumerClosed()) 0.0 else 1.0
    }

}