plugins {
    // Android Library
    alias(libs.plugins.playground.android.library)
    // Hilt
    alias(libs.plugins.playground.hilt)
    // Room
    alias(libs.plugins.playground.room)
}

android {
    namespace = "com.example.model"
}

dependencies {

}