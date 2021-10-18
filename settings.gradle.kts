pluginManagement {
    val kotlinVersion: String by settings
    val kspVersion: String by settings
    plugins {
        id("com.google.devtools.ksp") version kspVersion
        kotlin("jvm") version kotlinVersion
        id("org.springframework.boot") version "2.5.5"
        id("io.spring.dependency-management") version "1.0.11.RELEASE"
        kotlin("plugin.spring") version kotlinVersion
    }
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "jdb-auto-ksp-plugin"

include(":workload")
include(":processor")
