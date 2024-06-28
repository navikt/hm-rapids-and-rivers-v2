# hm-rapid-and-rivers-v2
## About
Rapids and Rivers (R&R) concept based on ideas of Fred George

## Core
Main NAV implementations

## Ktor
Setup R&R for use with Ktor

## Micronaut
Take a look at ApplicationTest to see how to run R&R within Micronaut Framework,

## Deadletter support
This only works for Micronaut. To enable deadletter support, you need to add the following dependency to your project:

```kotlin
    implementation("com.github.navikt:hm-rapids-and-rivers-v2-micronaut-deadletter:$rapidsRiversVersion")
```

And create a table in your database using the sql in src/test/resources/db/deadletter/V1:0__create_deadletter_table.sql
Then annotate the onpacket method with @DeadLetterSupport(packet = "packet", messageContext = "context")