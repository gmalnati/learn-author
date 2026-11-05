// Build of the Java web exercises (ADR 030): starters and solutions copy this file. The toolchain
// image caches exactly these plugins and libraries, so other versions would not build offline. The
// optional libraries an exercise may add are in the image's cache/build.gradle.kts.
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    id("org.springframework.boot") version "4.1.1"
    id("gg.jte.gradle") version "3.2.4"
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
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Pages: JTE templates in src/main/jte, compiled with the code, and htmx served by the jar.
    implementation("gg.jte:jte:3.2.4")
    implementation("gg.jte:jte-spring-boot-starter-4:3.2.4")
    implementation("org.webjars.npm:htmx.org:2.0.11")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// The templates are compiled to Java with the rest of the code: a mistake in a template fails the
// build, like a mistake in a class.
jte {
    generate()
    binaryStaticContent = true
}

tasks.test {
    useJUnitPlatform()
}

tasks.bootJar {
    archiveFileName = "app.jar"
}
