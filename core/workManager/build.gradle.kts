plugins {
    // Android Library
    alias(libs.plugins.playground.android.library)
    // Hilt
    alias(libs.plugins.playground.hilt)
}

android {
    namespace = "com.example.workmanager"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    // Dependencies
    implementation(libs.work.runtime)

    // Android Testing
    androidTestImplementation(libs.work.testing)
    androidTestImplementation(libs.bundles.androidTest)
}