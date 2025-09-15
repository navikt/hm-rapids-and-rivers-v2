val micronautVersion="4.9.3"

plugins {
    kotlin("kapt")
    id("io.micronaut.library") version "4.5.4"
}

dependencies {
    implementation(project(":hm-rapids-and-rivers-v2-core"))
    runtimeOnly("org.yaml:snakeyaml")
    implementation("io.micronaut:micronaut-jackson-databind")
    kapt("io.micronaut:micronaut-inject")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("org.apache.commons:commons-lang3:3.18.0")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("io.micronaut:micronaut-http-server-netty")
}

micronaut {
    version.set(micronautVersion)
    testRuntime("netty")
    testRuntime("junit5")
    processing {
        incremental(false)
        annotations("no.nav.hm.rapids_rivers.micronaut.*")
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
