
package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import no.nav.helse.rapids_rivers.KafkaConfig
import no.nav.helse.rapids_rivers.KafkaRapid

@Factory
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

}