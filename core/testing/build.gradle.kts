plugins {
    // Android Library
    alias(libs.plugins.playground.android.library)
}

android {
    namespace = "com.example.testing"
}

dependencies {
    // Data
    implementation(projects.data.theme)
    implementation(projects.data.preferences)
    implementation(projects.data.product)
    implementation(projects.data.notification)
    implementation(projects.core.workManager)
    implementation(libs.work.testing)

    // Core dependencies
    implementation(projects.core.model)

    // Testing dependencies
    implementation(libs.junit)
    implementation(libs.coroutines.test)

    // Android testing
    implementation(libs.androidx.test.runner)
    implementation(libs.hilt.android.testing)
}