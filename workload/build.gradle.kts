plugins {
    id("com.google.devtools.ksp")
    kotlin("jvm")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val jdbiVersion = "3.21.0"
    implementation(kotlin("stdlib"))
    implementation(project(":processor"))
    ksp(project(":processor"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jdbi:jdbi3-core:${jdbiVersion}")
    implementation("org.jdbi:jdbi3-sqlobject:${jdbiVersion}")
    implementation("org.jdbi:jdbi3-kotlin:${jdbiVersion}")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:${jdbiVersion}")
}

tasks.bootJar {
    enabled = false
}



kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}
