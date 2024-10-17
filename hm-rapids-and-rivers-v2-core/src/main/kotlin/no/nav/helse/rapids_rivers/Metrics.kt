package no.nav.helse.rapids_rivers

import io.prometheus.metrics.core.metrics.Counter
import io.prometheus.metrics.core.metrics.Histogram
import io.prometheus.metrics.model.registry.PrometheusRegistry


object Metrics {
    private val registry = PrometheusRegistry.defaultRegistry

    val onPacketHistorgram = Histogram.builder()
        .name("on_packet_seconds")
        .help("Hvor lang det tar å lese en gjenkjent melding i sekunder")
        .labelNames("rapid", "river", "event_name")
        .register(registry)

    val onMessageCounter = Counter.builder()
        .name("message_counter")
        .help("Hvor mange meldinger som er lest inn")
        .labelNames("rapid", "river", "validated")
        .register(registry)

}
