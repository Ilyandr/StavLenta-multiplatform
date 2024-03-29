// Инициализация зависимостей на уровне общей (ios / android) части.
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
}

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // Инициализация системы сборки для ios.
    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    // Инициализация зависимостей.
    sourceSets {

        // Versions
        val ktorVersion = "2.2.1"
        val coroutinesCoreVersion = "1.6.4"
        val base64Version = "1.0.6"
        val kodeinDI = "7.1.0"

        // Feature dependencies
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        // Оющие зависимости.
        val commonMain by getting {
            dependencies {
                // Ядро.
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesCoreVersion")

                // Инъекция зависимостей.
                implementation("org.kodein.di:kodein-di:$kodeinDI")

                // Rest взаимодействие.
                implementation("io.ktor:ktor-client-core:${ktorVersion}")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-auth:$ktorVersion")
                implementation("de.peilicke.sascha:kase64:$base64Version")

                // Обработка данных.
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }

        // Нативные зависимости.
        val androidMain by getting {
            dependencies {
                // Rest
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                // Rest
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
    }
}

android {
    namespace = "gcu.production.stavlenta"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
}