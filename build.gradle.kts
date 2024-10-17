import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("kapt") version "1.9.21"
    id("java")
    id("maven-publish")
}


subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.kapt")
        plugin("java")
        plugin("maven-publish")
    }

    val jvmTarget = "17"
    val kafkaVersion = "3.3.1"
    val micrometerRegistryPrometheusVersion = "1.13.1"
    val junitJupiterVersion = "5.9.1"
    val jacksonVersion = "2.14.1"
    val logbackClassicVersion = "1.4.5"
    val logbackEncoderVersion = "7.2"
    val awaitilityVersion = "4.2.0"
    val kafkaTestcontainerVersion = "1.19.6"

    group = "com.github.navikt"
    version = properties["version"] ?: "local-build"

    dependencies {
        api("ch.qos.logback:logback-classic:$logbackClassicVersion")
        api("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")

        api("org.apache.kafka:kafka-clients:$kafkaVersion")

        api("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
        api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
        api("io.micrometer:micrometer-core:$micrometerRegistryPrometheusVersion")
        api("io.micrometer:micrometer-registry-prometheus:$micrometerRegistryPrometheusVersion")

        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")

        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
        testImplementation("org.awaitility:awaitility:$awaitilityVersion")
        testImplementation("org.testcontainers:kafka:$kafkaTestcontainerVersion")
    }

    java {
        sourceCompatibility = JavaVersion.toVersion(jvmTarget)
        targetCompatibility = JavaVersion.toVersion(jvmTarget)

        withSourcesJar()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = jvmTarget
    }

    tasks.named<KotlinCompile>("compileTestKotlin") {
        kotlinOptions.jvmTarget = jvmTarget
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
        gradleVersion = "8.5"
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
