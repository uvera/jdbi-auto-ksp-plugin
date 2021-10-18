pluginManagement {
    val kotlinVersion: String by settings
    val kspVersion: String by settings
    val springBootVersion: String by settings
    val springDepManagementVersion: String by settings
    plugins {
        id("com.google.devtools.ksp") version kspVersion
        kotlin("jvm") version kotlinVersion
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDepManagementVersion
        kotlin("plugin.spring") version kotlinVersion
    }
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "jdb-auto-ksp-plugin"

include(":workload")
include(":processor")
