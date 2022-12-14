// Инициализация зависимостей на уровне проекта.
plugins {
    val kotlinVersion = "1.7.20"
    val gradleVersion = "7.3.1"

    id("com.android.application").version(gradleVersion).apply(false)
    id("com.android.library").version(gradleVersion).apply(false)
    kotlin("android").version(kotlinVersion).apply(false)
    kotlin("multiplatform").version(kotlinVersion).apply(false)
}

buildscript {

    // Источник зависимостей (сверху-вниз).
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
    // Основные зависимости.
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath(kotlin("serialization", version = "1.7.20"))
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
    }
}

// Дополнительные параметры для системы сборки.
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
