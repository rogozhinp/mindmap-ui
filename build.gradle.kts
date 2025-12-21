import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    kotlin("multiplatform") version "2.1.0"
    id("org.jetbrains.compose") version "1.7.3"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
    jacoco
    id("org.sonarqube") version "7.2.2.6593" // Verify if newer version is available/needed
}

group = "com.mind.ui"
version = "0.0.1-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

tasks.withType<JacocoReport>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("jvmTest")

    group = "Reporting"
    description = "Generate Jacoco coverage reports."

    executionData.setFrom(
        fileTree(layout.buildDirectory) {
            include("jacoco/jvmTest.exec", "jacoco/test.exec")
        }
    )

    sourceDirectories.setFrom(
        files(
            kotlin.sourceSets
                .getByName("jvmMain")
                .kotlin
                .srcDirs
        )
    )

    classDirectories.setFrom(
        fileTree("${layout.buildDirectory.get()}/classes/kotlin/jvm/main") {
            exclude(
                "**/config/**",
                "**/dto/**",
                "**/entity/**",
                "**/*Application*",
                "**/mapper/**",
                "**/*ComposableSingletons*"
            )
        }
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "mindmap-fe")
        property("sonar.projectName", "MindMap frontend")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.sourceEncoding", "UTF-8")

        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${layout.buildDirectory.get()}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
        )

        property(
            "sonar.exclusions",
            "**/config/**, **/dto/**, **/entity/**, **/*Application.kt, **/mapper/**"
        )
    }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "21"
        }
        withJava()

        testRuns["test"].executionTask.configure {
            finalizedBy(tasks.withType<JacocoReport>())
        }
    }

    macosArm64("mac")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation("io.ktor:ktor-client-core:2.3.12")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("io.ktor:ktor-client-cio:2.3.12")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
                implementation("io.ktor:ktor-serialization-jackson:2.3.12")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                // JUnit 5 API and Engine
                implementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")

                // Compose Testing Support
                implementation(compose.desktop.uiTestJUnit4)
                runtimeOnly("org.junit.vintage:junit-vintage-engine:5.10.2")

                // Ktor Mocking for TDD
                implementation("io.ktor:ktor-client-mock:2.3.12")

                // Mockito-Kotlin
                implementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    javaLauncher.set(javaToolchains.launcherFor(java.toolchain))
}

compose.desktop {
    application {
        mainClass = "com.mind.ui.MindMapAppKt"

        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Pkg
            )
            packageName = "MindMapUI"
            packageVersion = "1.0.0"
        }

        jvmArgs("-Dapple.awt.UIElement=false")
    }
}
