// Not an exercise: the toolchain image builds, tests and checks this project once, so that the
// Gradle cache also holds the optional libraries and tools an exercise may add to the template's
// build: SQLite or PostgreSQL with JDBC and Flyway (H2 in the tests), the Timefold Solver, ArchUnit,
// and the quality tools of `gradle check` (JaCoCo, Checkstyle, SpotBugs) and PIT. Same plugins and
// versions as the template; the tools have the versions an exercise must write.
import com.github.spotbugs.snom.Confidence
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    jacoco
    checkstyle
    id("org.springframework.boot") version "4.1.1"
    id("gg.jte.gradle") version "3.2.4"
    id("com.github.spotbugs") version "6.5.13"
    id("info.solidsoft.pitest") version "1.19.0"
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
    // The template's dependencies.
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("gg.jte:jte:3.2.4")
    implementation("gg.jte:jte-spring-boot-starter-4:3.2.4")
    implementation("org.webjars.npm:htmx.org:2.0.11")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // Optional: SQLite with JdbcClient and Flyway migrations.
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    runtimeOnly("org.xerial:sqlite-jdbc")
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc-test")
    // Optional: PostgreSQL with its Flyway module for the service, H2 for the tests of the build,
    // which has no database.
    implementation("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("com.h2database:h2")
    // Optional: the Timefold Solver; its ConstraintVerifier for the tests is in the same library.
    implementation("ai.timefold.solver:timefold-solver-core:2.7.1")
    // Optional: rules on the structure of the code, checked by the tests.
    testImplementation("com.tngtech.archunit:archunit-junit5:1.5.1")
    // Optional: @SuppressFBWarnings, to silence one finding of SpotBugs with a reason.
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.10.5")
}

jte {
    generate()
    binaryStaticContent = true
}

tasks.test {
    useJUnitPlatform()
}

// Optional: coverage with JaCoCo, a report and a threshold checked by `gradle check`. 0.8.15 is the
// version of Gradle 9.8.1, also without toolVersion. The classes that JTE generates from the
// templates are not code of the exercise.
jacoco {
    toolVersion = "0.8.15"
}
val coveredClasses = sourceSets.main.get().output.classesDirs.asFileTree.matching {
    exclude("gg/jte/generated/**")
}
tasks.jacocoTestReport {
    classDirectories.setFrom(coveredClasses)
    reports {
        xml.required = true
    }
}
tasks.jacocoTestCoverageVerification {
    classDirectories.setFrom(coveredClasses)
    violationRules {
        rule {
            limit {
                minimum = "0.5".toBigDecimal()
            }
        }
    }
}
tasks.check {
    dependsOn(tasks.jacocoTestReport, tasks.jacocoTestCoverageVerification)
}

// Optional: style rules with Checkstyle (config/checkstyle/checkstyle.xml). Gradle 9.8.1 would
// use 10.24.0, which is not in the cache: an exercise writes toolVersion.
checkstyle {
    toolVersion = "14.3.0"
    maxWarnings = 0
}
tasks.checkstyleMain {
    exclude("gg/jte/generated/**")
}

// Optional: common bugs with SpotBugs, on the compiled classes, with an HTML report.
spotbugs {
    toolVersion = "4.10.5"
    reportLevel = Confidence.MEDIUM
}
tasks.spotbugsMain {
    reports.create("html") {
        required = true
    }
}

// Optional: mutation testing with PIT and its JUnit 5 plugin.
pitest {
    pitestVersion = "1.30.1"
    junit5PluginVersion = "1.2.3"
    targetClasses = setOf("dev.learnlab.cache.Slots")
    targetTests = setOf("dev.learnlab.cache.SlotsTest")
    mutationThreshold = 100
    timestampedReports = false
    outputFormats = setOf("HTML", "XML")
}
