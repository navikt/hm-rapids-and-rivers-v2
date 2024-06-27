package no.nav.hm.rapids_rivers.micronaut.deadletter

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import java.time.LocalDateTime
import java.util.UUID
import no.nav.helse.rapids_rivers.JsonMessage

@MappedEntity("hm_dead_letter_v1")
data class DeadLetter(
    @field:Id
    val eventId: UUID,
    val eventName: String,
    val json: String,
    val error: String,
    val created: LocalDateTime = LocalDateTime.now(),
    val topic: String,
    val riverName: String
)
