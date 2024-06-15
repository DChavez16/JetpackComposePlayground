// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    // AGP Application
    alias(libs.plugins.android.application) apply false
    // AGP Library
    alias(libs.plugins.android.library) apply false
    // Kotlin
    alias(libs.plugins.kotlin.android) apply false
    // KSP
    alias(libs.plugins.ksp) apply false
    // Hilt
    alias(libs.plugins.dagger.hilt.android) apply false
    // Room
    alias(libs.plugins.room) apply false
}
true // Needed to make the Suppress annotation work for the plugins block