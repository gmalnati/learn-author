// Build of the Kotlin exercises of the base toolchain (ADR 027): starters and solutions copy this
// file. The toolchain image caches exactly these plugins and libraries, so other versions would not
// build offline; Spring Boot is in the image toolchain-kotlin-web.
plugins {
    kotlin("jvm") version "2.3.21"
    application
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// The program of the exercise: `fun main` in Main.kt, started by learn-run.
application {
    mainClass = "dev.learnlab.exercise.MainKt"
    applicationName = "app"
}

tasks.test {
    useJUnitPlatform()
}
