// Инициализация зависимостей на уровне нативной-android части.
plugins {
    kotlin("android")
    id("com.android.application")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "gcu.production.stavlenta.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "gcu.production.stavlenta.android"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {

    val ktorVersion = "2.2.1"
    val composeVersion = "1.3.2"
    implementation(project(":shared"))

    // core
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material:material:1.3.1")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.paging:paging-compose:1.0.0-alpha17")

    // navigation
    implementation("io.github.alexgladkov:odyssey-core:1.0.1")
    implementation("io.github.alexgladkov:odyssey-compose:1.0.1")

    // ui
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.24.13-rc")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("io.coil-kt:coil-gif:2.2.2")
    implementation("io.coil-kt:coil-compose:2.2.2")

    // di
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    kapt("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")
    implementation("com.google.dagger:hilt-android:2.44")

    // rest
    implementation("io.ktor:ktor-client-core:${ktorVersion}")
}