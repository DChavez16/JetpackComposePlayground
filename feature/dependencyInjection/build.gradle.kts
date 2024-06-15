plugins {
    // Feature
    alias(libs.plugins.playground.feature)
    // Library Compose
    alias(libs.plugins.playground.library.compose)
}

android {
    namespace = "com.example.dependencyinjection"
}

dependencies {
    // Dependencies
    implementation(projects.core.model)

    // Core dependencies
    implementation(projects.core.ui)
}