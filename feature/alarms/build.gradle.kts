plugins {
    // Feature
    alias(libs.plugins.playground.feature)
    // Library Compose
    alias(libs.plugins.playground.library.compose)
}

android {
    namespace = "com.feature.alarms"
}

dependencies {
    // Dependencies
    implementation(projects.core.ui)

    // Testing dependencies
    testImplementation(projects.core.testing)
    testImplementation(libs.junit)

    // Android testing
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.bundles.androidTest)
}