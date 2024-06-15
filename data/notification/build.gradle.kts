plugins {
    // Android Library
    alias(libs.plugins.playground.android.library)
    // Hilt
    alias(libs.plugins.playground.hilt)
}

android {
    namespace = "com.example.notification"
}

dependencies {
    // Dependencies
    implementation(projects.core.workManager)

    implementation(libs.work.runtime)
}