plugins {
    val kotlinVersion = "1.7.10"
    val gradleVersion = "7.3.1"
    val dependenciesInjectionVersion = "1.0.2"

    id("com.android.application").version(gradleVersion).apply(false)
    id("com.android.library").version(gradleVersion).apply(false)
    id("io.github.sergeshustoff.dikt").version(dependenciesInjectionVersion).apply(false)
    kotlin("android").version(kotlinVersion).apply(false)
    kotlin("multiplatform").version(kotlinVersion).apply(false)
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        val kotlinVersion = "1.7.10"

        classpath(kotlin("serialization", version = kotlinVersion))
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
