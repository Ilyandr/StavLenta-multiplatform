plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

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

    sourceSets {

        // Versions
        val ktorVersion = "2.2.1"
        val coroutinesCoreVersion = "1.6.4"
        val serializationCoreVersion = "1.1.0"
        val base64Version = "1.0.6"

        // Feature dependencies
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        // Base dependencies
        val commonMain by getting {
            dependencies {
                //Core
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationCoreVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesCoreVersion")

                // Rest
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-core:${ktorVersion}")
                implementation("io.ktor:ktor-client-json:${ktorVersion}")
                implementation("io.ktor:ktor-client-serialization:${ktorVersion}")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("de.peilicke.sascha:kase64:$base64Version")
            }
        }

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