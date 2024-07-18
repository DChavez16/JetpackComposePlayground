plugins {
    // Android Library
    alias(libs.plugins.playground.android.library)
    // Hilt
    alias(libs.plugins.playground.hilt)
    // Room
    alias(libs.plugins.playground.room)
    // Serialization
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.example.model"
}

dependencies {
    // Dependencies
    implementation(libs.kotlinx.serialization.json)
}