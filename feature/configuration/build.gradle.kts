plugins {
    // Feature
    alias(libs.plugins.playground.feature)
    // Library Compose
    alias(libs.plugins.playground.library.compose)
    // Hilt
    alias(libs.plugins.playground.hilt)
//    id("kotlin-kapt")
}

android {
    namespace = "com.example.configuration"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    // Data
    implementation(projects.data.theme)

    // Core dependencies
    implementation(projects.core.ui)

    // Testing dependencies
    testImplementation(projects.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)

    // Android testing
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.bundles.androidTest)
    debugImplementation(libs.compose.ui.test.manifest)
}