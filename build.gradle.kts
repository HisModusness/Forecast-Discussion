import net.wasdev.wlp.gradle.plugins.extensions.FeatureExtension
import net.wasdev.wlp.gradle.plugins.extensions.ServerExtension
import kotlin.collections.mapOf

plugins {
    kotlin("jvm") version "1.3.72"
    id("net.wasdev.wlp.gradle.plugins.Liberty")  version "2.6.5"
    war
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")
    providedCompile("javax", "javaee-api", "8.0")

    testImplementation("junit", "junit", "4.12")
    libertyRuntime("io.openliberty", "openliberty-runtime", "[20.0.0.3,)")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

val httpPort by extra { 8080 }
val httpsPort by extra { 9443 }
val applicationName by extra { (tasks["war"] as War).archiveName }

liberty {
    server = ServerExtension("forecastServer")
    server.apps = listOf(tasks["war"])
    server.bootstrapProperties = mapOf("httpPort" to httpPort, "httpsPort" to httpsPort, "applicationName" to applicationName)
    server.configDirectory = file("src/main/liberty/config")
}

war {
    val war = tasks["war"] as War
    war.archiveFileName.set("${war.archiveBaseName}.${war.archiveExtension}")
}

tasks["clean"].dependsOn("libertyStop")

