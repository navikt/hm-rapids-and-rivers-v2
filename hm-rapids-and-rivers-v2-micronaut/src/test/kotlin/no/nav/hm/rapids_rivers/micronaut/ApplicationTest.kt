package no.nav.hm.rapids_rivers.micronaut

import io.micronaut.runtime.Micronaut

object ApplicationTest {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .packages("no.nav.hm.rapids_rivers.micronaut")
            .mainClass(ApplicationTest.javaClass)
            .start()
    }
}