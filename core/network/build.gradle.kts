plugins {
    // Android Library
    alias(libs.plugins.playground.android.library)
    // Retrofit
    alias(libs.plugins.playground.retrofit)
    // Hilt
    alias(libs.plugins.playground.hilt)
}

android {
    namespace = "com.example.network"
}

dependencies {
    // Dependencies
    implementation(projects.core.model)
}