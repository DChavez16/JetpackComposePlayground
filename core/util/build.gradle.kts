plugins {
    // Android Library
    alias(libs.plugins.playground.android.library)
    // Library Compose
    alias(libs.plugins.playground.library.compose)
}

android {
    namespace = "com.example.util"
}

dependencies {
    // Dependencies
    implementation(libs.core.ktx)
}