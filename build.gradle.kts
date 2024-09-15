plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktorVersion: String by project
val slf4jVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project
val junitApiVersion : String by project
val mockitoVersion: String by project
dependencies {
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")

    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitApiVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}