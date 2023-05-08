
package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import jakarta.inject.Singleton
import no.nav.helse.rapids_rivers.KafkaConfig
import no.nav.helse.rapids_rivers.KafkaRapid
import no.nav.helse.rapids_rivers.KafkaRapidMetrics

@Factory
@Requires(property = "rapidsandrivers.enabled", notEquals="false", defaultValue = "true")
class RapidsRiversFactory {

    @Singleton
    fun createKafkaRapid(kafkaProps: KafkaProperties): KafkaRapid {
        val kafkaConfig = KafkaConfig(
            bootstrapServers =kafkaProps.brokers,
            consumerGroupId = kafkaProps.consumerGroupId,
            clientId = kafkaProps.clientId,
            truststore = kafkaProps.trustStorePath,
            truststorePassword = kafkaProps.trustStorePassword,
            keystoreLocation = kafkaProps.keystorePath,
            keystorePassword = kafkaProps.keystorePassword)
        return KafkaRapid.create(kafkaConfig, kafkaProps.topic, kafkaProps.extraTopics)
    }

    @Bean
    @Singleton
    @Primary
    fun rapidMetrics(kafkaRapid: KafkaRapid): KafkaRapidMetrics = KafkaRapidMetrics(kafkaRapid)

}
