import net.wasdev.wlp.gradle.plugins.extensions.FeatureExtension
import net.wasdev.wlp.gradle.plugins.extensions.ServerExtension
import kotlin.collections.mapOf

plugins {
    kotlin("jvm") version "1.2.51"
    id("net.wasdev.wlp.gradle.plugins.Liberty")  version "2.2"
    war
}

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    providedCompile("javax", "javaee-api", "8.0")

    testImplementation("junit", "junit", "4.12")
    libertyRuntime("io.openliberty", "openliberty-runtime", "[18.0.0.2,)")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

val httpPort by extra { 12100 }
val httpsPort by extra { 12105 }
val applicationName by extra { (tasks["war"] as War).archiveName }

liberty {
    server = ServerExtension()
    server.name = "forecastServer"
    server.apps = listOf(tasks["war"])
    server.bootstrapProperties = mapOf("httpPort" to httpPort, "httpsPort" to httpsPort, "applicationName" to applicationName)
    server.configDirectory = file("src/main/liberty/config")
}

war {
    val war = tasks["war"] as War
    war.archiveName = "${war.baseName}.${war.extension}"
}

tasks["clean"].dependsOn("libertyStop")

