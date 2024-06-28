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

    companion object {
        private val LOG = LoggerFactory.getLogger(DeadLetterMethodInterceptor::class.java)
    }

    override fun intercept(context: MethodInvocationContext<Any, Any>): Any? {
        try {

            LOG.debug("Executingtarget method: ${context.targetMethod} in class ${context.targetMethod.declaringClass.name} with arguments ${context.parameters}")
            return context.proceed()
        }
        catch (e: Exception) {
            val riverName = context.targetMethod.declaringClass.name
            LOG.error("Error executing method ${context.targetMethod}", e)
            val annotation = context.targetMethod.getAnnotation(DeadLetterSupport::class.java)!!
            val packet = context.parameters[annotation.packet]!!.value as JsonMessage
            val messageContext = context.parameters[annotation.meesageContext]!!.value as MessageContext
            runBlocking {
                deadLetterRepository.save(
                    DeadLetter(
                        eventId = UUID.randomUUID(),
                        eventName = context.targetMethod.name,
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
