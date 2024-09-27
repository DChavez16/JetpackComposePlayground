plugins {
    // Android Application
    alias(libs.plugins.playground.android.application)
    // Application Compose
    alias(libs.plugins.playground.application.compose)
    // Hilt dependency injection
    alias(libs.plugins.playground.hilt)
}

android {
    namespace = "com.example.mobile"

    defaultConfig {
        applicationId = "com.example.mobile"
        versionCode = 1
        versionName = "0.0.0" // X.Y.Z; X = Major, Y = Minor, Z = Patch
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    // Feature
    implementation(projects.feature.navigationUi)
    implementation(projects.feature.configuration)
    implementation(projects.feature.lazyLayouts)
    implementation(projects.feature.animations)
    implementation(projects.feature.drawScope)
    implementation(projects.feature.themes)
    implementation(projects.feature.localDatabase)
    implementation(projects.feature.remoteDatabase)
    implementation(projects.feature.dataPersistence)
    implementation(projects.feature.persistentWork)

    // Data
    implementation(projects.data.theme)

    // Core
    implementation(projects.core.ui)
    implementation(projects.core.util)

    // Dependencies
    implementation(libs.navigation.compose)

    // Dependencies
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)

    // Android testing
    androidTestImplementation(libs.bundles.androidTest)
    debugImplementation(libs.compose.ui.test.manifest)
}
