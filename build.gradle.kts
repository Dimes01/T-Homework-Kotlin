plugins {
    kotlin("jvm") version "2.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

val junitApiVersion : String by project
val logbackVersion: String by project
subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        implementation("ch.qos.logback:logback-classic:$logbackVersion")
        testImplementation(kotlin("test"))
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitApiVersion")
    }
}



tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}