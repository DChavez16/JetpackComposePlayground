plugins {
    // Feature
    alias(libs.plugins.playground.feature)
    // Library Compose
    alias(libs.plugins.playground.library.compose)
    // Hilt
    alias(libs.plugins.playground.hilt)
}

android {
    namespace = "com.feature.widgets"
}

dependencies {
    // Dependencies
    implementation(projects.core.ui)
    implementation(projects.data.notes)
    implementation(libs.bundles.glaceMaterial3)

    // Core dependencies
    implementation(projects.core.model)

    // Testing dependencies
    testImplementation(projects.core.testing)
    testImplementation(libs.junit)

    // Android testing
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.bundles.androidTest)
}