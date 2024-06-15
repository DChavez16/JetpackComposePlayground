plugins {
    // Android Library
    alias(libs.plugins.playground.android.library)
    // Hilt
    alias(libs.plugins.playground.hilt)
}

android {
    namespace = "com.example.preferences"
}

dependencies {
    // Dependencies
    implementation(projects.core.dataStore)
}