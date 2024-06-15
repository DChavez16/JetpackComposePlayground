plugins {
    // Android Library
    alias(libs.plugins.playground.android.library)
    // Room
    alias(libs.plugins.playground.room)
    // Hilt
    alias(libs.plugins.playground.hilt)
}

android {
    namespace = "com.example.database"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    // Dependencies
    implementation(projects.core.model)

    // Android testing
    androidTestImplementation(libs.bundles.androidTest)
}