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
                name.set("hm-rapids-rivers-v2-core")
                description.set("hm rapids and rivers v2")
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
