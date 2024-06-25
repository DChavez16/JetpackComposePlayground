plugins {
    // Feature
    alias(libs.plugins.playground.feature)
    // Library Compose
    alias(libs.plugins.playground.library.compose)
}

android {
    namespace = "com.example.datapersistence"
}

dependencies {
    // Dependencies
    implementation(projects.data.preferences)

    // Core dependencies
    implementation(projects.core.ui)

    // Android Color Picker
    implementation(libs.bundles.colorPicker)

    // Testing dependencies
    testImplementation(projects.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)

    // Android testing
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.bundles.androidTest)
    debugImplementation(libs.compose.ui.test.manifest)
}