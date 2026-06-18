
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("org.jetbrains.kotlin.plugin.allopen") version "2.3.0"
    id("com.google.devtools.ksp") version "2.3.0"
    id("com.gradleup.shadow") version "9.3.1"
    id ("com.github.ben-manes.versions") version "0.51.0"
    id("java")
    id("maven-publish")
}


subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("com.google.devtools.ksp")
        plugin("java")
        plugin("maven-publish")
    }

    val jvmTarget = "25"
    val kafkaVersion = "4.2.0"
    val micrometerRegistryPrometheusVersion = "1.13.1"
    val junitJupiterVersion = "5.9.1"
    val jacksonVersion = "3.1.3"
    val logbackClassicVersion = "1.4.12"
    val logbackEncoderVersion = "8.1"
    val awaitilityVersion = "4.2.0"
    val kafkaTestcontainerVersion = "2.0.1"

    group = "com.github.navikt"
    version = properties["version"] ?: "local-build"

    dependencies {
        api("ch.qos.logback:logback-classic:$logbackClassicVersion")
        api("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")

        api("org.apache.kafka:kafka-clients:$kafkaVersion")

        api("tools.jackson.module:jackson-module-kotlin:$jacksonVersion")
        api("io.micrometer:micrometer-core:$micrometerRegistryPrometheusVersion")
        api("io.micrometer:micrometer-registry-prometheus:$micrometerRegistryPrometheusVersion")

        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")

        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
        testImplementation("org.awaitility:awaitility:$awaitilityVersion")
        testImplementation("org.testcontainers:testcontainers-kafka:$kafkaTestcontainerVersion")
    }

    java {
        sourceCompatibility = JavaVersion.toVersion(jvmTarget)
        targetCompatibility = JavaVersion.toVersion(jvmTarget)

        withSourcesJar()
    }

    tasks.withType<KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.fromTarget(jvmTarget))
    }

    tasks.named<KotlinCompile>("compileTestKotlin") {
        compilerOptions.jvmTarget.set(JvmTarget.fromTarget(jvmTarget))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("skipped", "failed")
            showExceptions = true
            showStackTraces = true
            showCauses = true
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
        }
    }

    tasks.withType<Wrapper> {
        gradleVersion = "9.5.0"
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://packages.confluent.io/maven/")
    }
}

repositories {
    mavenCentral()
}
