package no.nav.hm.rapids_rivers.micronaut

import java.util.UUID

interface RapidPushService {
    fun <T : Any> pushToRapid(key: String?=null, eventName: String?=null, payload: T,
                              keyValues : Map<String, Any> = emptyMap(), eventId: UUID = UUID.randomUUID())
}