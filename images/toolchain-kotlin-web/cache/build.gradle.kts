// Not an exercise: the toolchain image builds and tests this project once, so that the Gradle
// cache also holds the optional libraries an exercise may add to the template's build
// (persistence, migrations, security, coroutines, Actuator, test doubles, Spring Modulith, OpenAPI).
// Same plugins and versions as the template, plus the JPA plugin.
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.spring") version "2.3.21"
    kotlin("plugin.jpa") version "2.3.21"
    id("org.springframework.boot") version "4.1.1"
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    // The template's dependencies.
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // Optional: PostgreSQL with JPA or JDBC, and Flyway migrations.
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testRuntimeOnly("com.h2database:h2")
    // Optional: Spring Security with a JWT resource server.
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    // Optional: coroutines in controllers, and Actuator.
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // Optional: test doubles with MockK and property-based tests with jqwik.
    testImplementation("io.mockk:mockk:1.14.11")
    testImplementation("net.jqwik:jqwik:1.10.1")
    // Optional: the modules of a modular monolith with Spring Modulith (event publication registry on
    // JDBC), and the OpenAPI document of the API with springdoc. An exercise declares the same BOM.
    implementation(platform("org.springframework.modulith:spring-modulith-bom:2.1.1"))
    implementation("org.springframework.modulith:spring-modulith-starter-core")
    implementation("org.springframework.modulith:spring-modulith-starter-jdbc")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:3.1.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.bootJar {
    archiveFileName = "app.jar"
}
