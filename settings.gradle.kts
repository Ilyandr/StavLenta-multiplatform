// Основные настройки системы сборки (gradle) на уровне проекта.
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "StavLenta"
include(":androidApp")
include(":shared")