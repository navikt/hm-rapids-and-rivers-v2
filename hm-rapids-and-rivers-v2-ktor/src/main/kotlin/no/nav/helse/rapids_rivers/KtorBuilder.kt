package no.nav.helse.rapids_rivers

import io.ktor.server.application.log
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineEnvironmentBuilder
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.response.respondTextWriter
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.core.instrument.binder.MeterBinder
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.logging.LogbackMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import org.slf4j.Logger

class KtorBuilder {
    private val builder = ApplicationEngineEnvironmentBuilder()
    private val extraMeterBinders = mutableListOf<MeterBinder>()

    fun port(port: Int) = apply {
        builder.connector {
            this.port = port
        }
    }

    fun log(logger: Logger) = apply {
        builder.log = logger
    }

    fun module(module: Application.() -> Unit) = apply {
        builder.module(module)
    }

    fun <TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration> build(factory: ApplicationEngineFactory<TEngine, TConfiguration>): ApplicationEngine = embeddedServer(factory, applicationEngineEnvironment {
        module {
            install(MicrometerMetrics) {
                registry = DefaultMeterRegistry.Default
                meterBinders = listOf(
                    ClassLoaderMetrics(),
                    JvmMemoryMetrics(),
                    JvmGcMetrics(),
                    ProcessorMetrics(),
                    JvmThreadMetrics(),
                    LogbackMetrics()
                ) + extraMeterBinders
            }

            routing {
                get("/metrics") {
                    call.respond(DefaultMeterRegistry.Default.scrape())
                }
            }
        }

        this.connectors.addAll(builder.connectors)
        this.log = builder.log
        this.modules.addAll(builder.modules)
    })

    fun preStopHook(preStopHook: suspend () -> Unit) = apply {
        builder.module {
            val logger = log
            routing {
                get("/stop") {
                    logger.info("Received shutdown signal via preStopHookPath, calling actual stop hook")
                    preStopHook()
                    logger.info("Stop hook returned, responding to preStopHook request with 200 OK")
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }

    fun liveness(isAliveCheck: () -> Boolean) = apply {
        builder.module {
            routing {
                get("/isalive") {
                    if (!isAliveCheck()) return@get call.respondText(
                        "NOT ALIVE",
                        ContentType.Text.Plain,
                        HttpStatusCode.ServiceUnavailable
                    )
                    call.respondText("ALIVE", ContentType.Text.Plain)
                }
            }
        }
    }

    fun readiness(isReadyCheck: () -> Boolean) = apply {
        builder.module {
            routing {
                get("/isready") {
                    if (!isReadyCheck()) return@get call.respondText(
                        "NOT READY",
                        ContentType.Text.Plain,
                        HttpStatusCode.ServiceUnavailable
                    )
                    call.respondText("READY", ContentType.Text.Plain)
                }
            }
        }
    }

    fun metrics(metrics: List<MeterBinder>) = apply {
        extraMeterBinders.addAll(metrics)
    }
}
