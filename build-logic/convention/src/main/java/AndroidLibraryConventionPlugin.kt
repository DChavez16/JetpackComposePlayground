
import com.android.build.api.dsl.LibraryExtension
import com.example.apps.configureAndroid
import com.example.apps.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


class AndroidLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Plugins
            with(pluginManager) {
                // Android Library
                apply("com.android.library")
                // Kotlin
                apply("org.jetbrains.kotlin.android")
                // Hilt
                apply("playground.hilt")
            }

            // Android Configuration
            extensions.configure<LibraryExtension> {
                configureAndroid(this)
                @Suppress("UnstableApiUsage")
                testOptions.targetSdk = 34
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
            }
        }
    }
}