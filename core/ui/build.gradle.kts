plugins {
    // Android Library
    alias(libs.plugins.playground.android.library)
    // Library Compose
    alias(libs.plugins.playground.library.compose)
}

android {
    namespace = "com.example.ui"
}

dependencies {
    implementation(projects.data.theme)

    // Dependencies
    implementation(libs.core.ktx)
    implementation(libs.ui.text.google.fonts)

    implementation(libs.bundles.lifecycle)
}