// Build of the Java exercises of the base toolchain (ADR 030): starters and solutions copy this
// file. The toolchain image caches exactly these plugins and libraries, so other versions would not
// build offline; Spring Boot is in the image toolchain-java-web.
plugins {
    java
    application
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// The program of the exercise: `main` in Main.java, started by learn-run.
application {
    mainClass = "dev.learnlab.exercise.Main"
    applicationName = "app"
}

tasks.test {
    useJUnitPlatform()
}
