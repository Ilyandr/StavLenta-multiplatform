plugins {
    val kotlinVersion = "1.7.20"
    val gradleVersion = "7.3.1"

    id("com.android.application").version(gradleVersion).apply(false)
    id("com.android.library").version(gradleVersion).apply(false)
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
        val kotlinVersion = "1.7.20"

        classpath(kotlin("serialization", version = kotlinVersion))
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
