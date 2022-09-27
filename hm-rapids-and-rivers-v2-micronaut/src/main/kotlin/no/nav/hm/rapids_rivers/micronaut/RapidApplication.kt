
package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import no.nav.helse.rapids_rivers.KafkaConfig
import no.nav.helse.rapids_rivers.KafkaRapid

@Factory
class RapidApplication {

    @Bean
    fun kafkaConfig(kafkaProps: KafkaProperties) = KafkaConfig(
            bootstrapServers =kafkaProps.brokers,
            consumerGroupId = kafkaProps.consumerGroupId,
            clientId = kafkaProps.clientId,
            truststore = kafkaProps.trustStorePath,
            truststorePassword = kafkaProps.trustStorePassword,
            keystoreLocation = kafkaProps.keystorePath,
            keystorePassword = kafkaProps.keystorePassword
    )

    @Bean
    fun createKafkaRapid(kafkaConfig: KafkaConfig, kafkaProps: KafkaProperties) =
        KafkaRapid.create(kafkaConfig, kafkaProps.topic, kafkaProps.extraTopics)

}