plugins {
    // Feature
    alias(libs.plugins.playground.feature)
    // Library Compose
    alias(libs.plugins.playground.library.compose)
}

android {
    namespace = "com.example.remotedatabase"
}

dependencies {
    // Dependencies
    implementation(projects.data.notes)
    implementation(projects.data.userTag)

    // Core dependencies
    implementation(projects.core.model)
    implementation(projects.core.ui)

    implementation(libs.navigation.compose)

    // Testing dependencies
    testImplementation(projects.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)

    // Android testing
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.bundles.androidTest)
    androidTestImplementation(libs.hilt.android.testing)

    // Debug
    debugImplementation(libs.compose.ui.test.manifest)
    debugImplementation(libs.androidx.test.runner)
    debugImplementation(libs.hilt.android.testing)
}