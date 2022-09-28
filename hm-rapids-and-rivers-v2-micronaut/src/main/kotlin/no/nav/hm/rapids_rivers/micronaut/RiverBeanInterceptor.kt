package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.aop.*
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Factory
import no.nav.helse.rapids_rivers.RapidsConnection

@Factory
class RiverBeanInterceptor(private val rapidsConnection: RapidsConnection) {

    @InterceptorBean(RiverBean::class)
    fun aroundInvoke(): MethodInterceptor<River, Any> { //
        return MethodInterceptor { context: MethodInvocationContext<River, Any> ->
            val river = context.target
            return@MethodInterceptor when (context.kind) {
                InterceptorKind.POST_CONSTRUCT -> {
                    river.register()
                    context.proceed()
                }
                else -> context.proceed()
            }
        }
    }

}