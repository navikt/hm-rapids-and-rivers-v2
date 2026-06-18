package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import no.nav.helse.rapids_rivers.RiverMetrics
import no.nav.helse.rapids_rivers.testsupport.TestRapid

@Factory
class KafkaRapidTestSetup {

    @Singleton
    fun createTestRapid(): TestRapid = TestRapid()

    @Singleton
    fun riverMetrics(): RiverMetrics = RiverMetrics()
}