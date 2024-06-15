plugins {
    // Android Library
    alias(libs.plugins.playground.android.library)
    // Hilt
    alias(libs.plugins.playground.hilt)
}

android {
    namespace = "com.example.product"
}

dependencies {
    // Dependencies
    implementation(projects.core.model)
    implementation(projects.core.database)
}