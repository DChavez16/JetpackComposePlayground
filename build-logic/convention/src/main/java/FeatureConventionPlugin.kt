import com.android.build.api.dsl.LibraryExtension
import com.example.apps.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies


class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Plugin
            pluginManager.apply {
                // Android Library
                apply("playground.android.library")
                // Hilt
                apply("playground.hilt")
            }

            // Configuration
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
            }

            // Dependencies
            dependencies {
                // Core UI
                "implementation"(project(":core:ui"))

                // Lifecycle
                "implementation"(libs.findBundle("lifecycle").get())
                // Hilt Navigation Compose
                "implementation"(libs.findLibrary("hilt.navigation.compose").get())

                // Lifecycle Android Test
                "androidTestImplementation"(libs.findLibrary("lifecycle.runtime.testing").get())
            }
        }
    }
}