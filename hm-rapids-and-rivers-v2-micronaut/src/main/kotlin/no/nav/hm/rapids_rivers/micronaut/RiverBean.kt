package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.aop.AroundConstruct
import io.micronaut.aop.InterceptorBinding
import io.micronaut.aop.InterceptorBindingDefinitions
import io.micronaut.aop.InterceptorKind
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.DefaultScope





@DefaultScope(Context::class)
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@AroundConstruct
@InterceptorBindingDefinitions(
    InterceptorBinding(kind = InterceptorKind.POST_CONSTRUCT)
)
annotation class RiverBean