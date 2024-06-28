package no.nav.hm.rapids_rivers.micronaut.deadletter

import io.micronaut.aop.Around
import io.micronaut.context.annotation.Type

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Around
@Type(DeadLetterMethodInterceptor::class)
annotation class DeadLetterSupport(
    /**
     *     The name of the parameter that contains the messageContext
     */
    val messageContext: String = "context",

    /**
     *  The name of the parameter that contains the packet
      */
    val packet: String = "packet"
)
