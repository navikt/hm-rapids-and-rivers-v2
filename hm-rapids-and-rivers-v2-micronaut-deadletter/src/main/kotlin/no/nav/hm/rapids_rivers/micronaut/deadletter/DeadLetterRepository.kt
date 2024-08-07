package no.nav.hm.rapids_rivers.micronaut.deadletter

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

@JdbcRepository(dialect = Dialect.POSTGRES)
interface DeadLetterRepository: CoroutineCrudRepository<DeadLetter, String>