plugins {
    // Feature
    alias(libs.plugins.playground.feature)
    // Library Compose
    alias(libs.plugins.playground.library.compose)
}

android {
    namespace = "com.example.workmanager"
}

dependencies {
    // Dependencies
    implementation(projects.core.ui)
    implementation(projects.data.notification)

    // Testing dependencies
    testImplementation(projects.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)

    // Android testing
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.bundles.androidTest)
    androidTestImplementation(libs.work.testing)
    debugImplementation(libs.compose.ui.test.manifest)
}