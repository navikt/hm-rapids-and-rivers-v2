package no.nav.hm.rapids_rivers.micronaut.deadletter

import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import jakarta.inject.Singleton
import java.util.UUID
import kotlinx.coroutines.runBlocking
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.MessageContext
import org.slf4j.LoggerFactory

@Singleton
class DeadLetterMethodInterceptor(private val deadLetterRepository: DeadLetterRepository): MethodInterceptor<Any, Any> {
    private var exceptionCount = 0

    companion object {
        private val LOG = LoggerFactory.getLogger(DeadLetterMethodInterceptor::class.java)
    }

    override fun intercept(context: MethodInvocationContext<Any, Any>): Any? {
        try {

            LOG.debug("Executingtarget method: ${context.targetMethod} in class ${context.targetMethod.declaringClass.name} with arguments ${context.parameters}")
            return context.proceed()
        }
        catch (e: Exception) {
            exceptionCount++
            val riverName = context.targetMethod.declaringClass.simpleName
            LOG.error("Error executing method ${context.targetMethod}", e)
            val annotation = context.targetMethod.getAnnotation(DeadLetterSupport::class.java)!!
            val packet = context.parameters[annotation.packet]!!.value as JsonMessage
            val eventId = packet["eventId"].asText() ?: UUID.randomUUID().toString()
            val eventName = packet["eventName"].asText() ?: riverName
            val messageContext = context.parameters[annotation.messageContext]!!.value as MessageContext

            if (exceptionCount>annotation.exceptionsToCatch) {
                LOG.error("Error count exceeded ${annotation.exceptionsToCatch}, stopping execution")
                throw e
            }
            runBlocking {
                deadLetterRepository.save(
                    DeadLetter(
                        eventId = eventId,
                        eventName = eventName,
                        json = packet.toJson(),
                        error = e.message ?: e.javaClass.name,
                        topic = messageContext.rapidName(),
                        riverName = riverName
                    )
                )
            }
        }
        return null
    }

}
