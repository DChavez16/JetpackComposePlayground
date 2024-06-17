plugins {
    // Feature
    alias(libs.plugins.playground.feature)
    // Library Compose
    alias(libs.plugins.playground.library.compose)
}

android {
    namespace = "com.example.navigationdrawer"
}

dependencies {
    // Data
    implementation(projects.data.theme)

    // Core dependencies
    implementation(projects.core.ui)
    implementation(projects.core.util)

    // Testing dependencies
    testImplementation(projects.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)

    // Android testing
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.bundles.androidTest)
    debugImplementation(libs.compose.ui.test.manifest)
}