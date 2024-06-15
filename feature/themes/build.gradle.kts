plugins {
    // Feature
    alias(libs.plugins.playground.feature)
    // Library Compose
    alias(libs.plugins.playground.library.compose)
}

android {
    namespace = "com.example.themes"
}

dependencies {
    // Core dependencies
    implementation(projects.core.ui)
}