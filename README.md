# Jdbi Auto KSP Plugin for Spring

[![](https://jitpack.io/v/uvera/jdbi-auto-ksp-plugin.svg)](https://jitpack.io/#uvera/jdbi-auto-spring-ksp-plugin)

Jdbi Auto is a [ KSP ](https://github.com/google/ksp) plugin which automatically generates JDBI SQL Object interfaces using source
code generation by scanning for provided annotation and generating Spring @Configuration classes.

## Quick start

Add `https://jitpack.to` to your build tool maven repositories.

More info at [ jitpack.io repository ](https://jitpack.io/#uvera/jdbi-auto-spring-ksp-plugin)

### Gradle setup

```kotlin
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
...

dependencies {
    implementation("com.github.uvera:jdbi-auto-spring-ksp-plugin:VERSION")
    ksp("com.github.uvera:jdbi-auto-spring-ksp-plugin:VERSION")
}
...

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}
```

### Code sample

For the following sample interface

```kotlin
import io.uvera.jdbiauto.JdbiAuto

...

@JdbiAuto
interface UserDao {
    ...
}
```

the following class will be generated

```kotlin

import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.jdbi.v3.sqlobject.kotlin.onDemand

@Configuration
class UserDaoAutoJdbiConfiguration(private val jdbi: Jdbi) {

    @Bean
    fun jdbiAutoBeanUserDao(): UserDao = jdbi.onDemand()
}
```
