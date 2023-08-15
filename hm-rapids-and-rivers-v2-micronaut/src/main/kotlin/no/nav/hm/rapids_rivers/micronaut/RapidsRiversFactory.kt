
package no.nav.hm.rapids_rivers.micronaut

import io.micrometer.core.instrument.binder.kafka.KafkaClientMetrics
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import jakarta.inject.Named
import jakarta.inject.Singleton
import no.nav.helse.rapids_rivers.KafkaConfig
import no.nav.helse.rapids_rivers.KafkaRapid
import no.nav.helse.rapids_rivers.KafkaRapidMetrics
import org.slf4j.LoggerFactory

@Factory
@Requires(property = "rapidsandrivers.enabled", notEquals="false", defaultValue = "true")
class RapidsRiversFactory {

    companion object {
        private val LOG = LoggerFactory.getLogger(RapidsRiversFactory::class.java)
    }
    @Singleton
    fun createKafkaRapid(kafkaProps: KafkaProperties): KafkaRapid {
        LOG.info("Creating kafka rapid service with bootstrap server to: ${kafkaProps.brokers}")
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

    @Singleton
    fun rapidMetrics(kafkaRapid: KafkaRapid): KafkaRapidMetrics = kafkaRapid.getRapidMetric()

    @Singleton
    @Named("ConsumerMetric")
    fun consumerMetric(kafkaRapid: KafkaRapid): KafkaClientMetrics = kafkaRapid.getConsumerMetric()

    @Singleton
    @Named("ProducerMetric")
    fun producerMetric(kafkaRapid: KafkaRapid): KafkaClientMetrics = kafkaRapid.getProducerMetric()


}
