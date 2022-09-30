package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.context.annotation.ConfigurationProperties
import java.net.InetAddress


@ConfigurationProperties("kafka")
class KafkaProperties {
    var brokers: String = "localhost:9092"
    var clientId: String = InetAddress.getLocalHost().hostName
    var consumerGroupId: String = "myGroupId"
    var topic: String = "rapid-topic-v1"
    var extraTopics: List<String> = emptyList()
    var trustStorePath: String? = null
    var trustStorePassword: String? = null
    var keystorePath: String? = null
    var keystorePassword: String? = null
}