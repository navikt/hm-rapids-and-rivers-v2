val micronautVersion="4.9.3"
val junitJupiterVersion = "5.9.2"
val tcVersion = "1.17.6"
val postgresqlVersion = "42.7.2"

plugins {
    id("io.micronaut.library") version "4.5.4"
}

dependencies {
    kapt("io.micronaut:micronaut-inject")
    implementation("io.micronaut:micronaut-context")
    implementation(project(":hm-rapids-and-rivers-v2-core"))
    implementation(project(":hm-rapids-and-rivers-v2-micronaut"))
    runtimeOnly("org.yaml:snakeyaml")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("org.postgresql:postgresql:${postgresqlVersion}")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    runtimeOnly("io.micronaut.sql:micronaut-jdbc-hikari")
    kapt("io.micronaut.data:micronaut-data-processor")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")

    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("org.flywaydb:flyway-database-postgresql:10.6.0")

    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.testcontainers:postgresql:${tcVersion}")

}

micronaut {
    version.set(micronautVersion)
    testRuntime("junit5")
    processing {
        incremental(false)
        annotations("no.nav.hm.rapids_rivers.micronaut.deadletter.*")
    }
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
                name.set("hm-rapids-rivers-v2-micronaut-deadletter")
                description.set("hm rapids and rivers v2 micronaut deadletter setup")
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
