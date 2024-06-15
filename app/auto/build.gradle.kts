plugins {
    // Android Application
    alias(libs.plugins.playground.android.application)
    // Application Compose
    alias(libs.plugins.playground.application.compose)
}

android {
    namespace = "com.example.auto"

    defaultConfig {
        applicationId = "com.example.auto"
        versionCode = 1
        versionName = "0.0.0" // X.Y.Z; X = Major, Y = Minor, Z = Patch
    }
}

dependencies {
}