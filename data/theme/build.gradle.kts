plugins {
    // Android Library
    alias(libs.plugins.playground.android.library)
    // Hilt
    alias(libs.plugins.playground.hilt)
}

android {
    namespace = "com.example.data.theme"
}

dependencies {
    // Dependencies
    implementation(projects.core.dataStore)
}