import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.archivesName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


val jvmTarget = "17"
val micronautVersion="3.7.0"
val kafkaVersion = "3.2.1"
val micrometerRegistryPrometheusVersion = "1.9.1"
val junitJupiterVersion = "5.9.0"
val jacksonVersion = "2.13.3"
val logbackClassicVersion = "1.2.11"
val logbackEncoderVersion = "7.2"
val kafkaEmbeddedVersion = "3.2.1"
val awaitilityVersion = "4.2.0"

group = "com.github.navikt"
version = properties["version"] ?: "local-build"

plugins {
    kotlin("jvm") version "1.7.0"
    kotlin("kapt") version "1.7.0"
    kotlin("plugin.allopen") version "1.7.0"
    id("java")
    id("maven-publish")
    id("io.micronaut.library") version "3.6.1"
}


dependencies {
    implementation(project(":hm-rapids-and-rivers-v2-core"))

    api("ch.qos.logback:logback-classic:$logbackClassicVersion")
    api("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")

    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("io.micronaut:micronaut-http-server-netty")
    testImplementation("no.nav:kafka-embedded-env:$kafkaEmbeddedVersion")
    {
        exclude("log4j")
        exclude("org.glassfish")
        exclude("io.netty")
    }
    testImplementation("org.awaitility:awaitility:$awaitilityVersion")
}

micronaut {
    version.set(micronautVersion)
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("no.nav.hm.rapids_rivers.micronaut.*")
    }
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
    gradleVersion = "7.5.1"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://packages.confluent.io/maven/")

}

val githubUser: String? by project
val githubPassword: String? by project

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/hm-rapids-and-rivers-v2")
            credentials {
                username = githubUser
                password = githubPassword
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {

            pom {
                name.set("hm-rapids-rivers-v2-micronaut")
                description.set("hm rapids and rivers v2 micronaut setup")
                url.set("https://github.com/navikt/hm-rapids-and-rivers-v2")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/navikt/hm-rapids-and-rivers-v2.git")
                    developerConnection.set("scm:git:https://github.com/navikt/hm-rapids-and-rivers-v2.git")
                    url.set("https://github.com/navikt/hm-rapids-and-rivers-v2")
                }
            }
            from(components["java"])
        }
    }
}
