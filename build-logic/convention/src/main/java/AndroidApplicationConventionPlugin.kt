import com.android.build.api.dsl.ApplicationExtension
import com.example.apps.configureAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Plugins
            with(pluginManager) {
                // Android Application
                apply("com.android.application")
                // Kotlin
                apply("org.jetbrains.kotlin.android")
                // Hilt
                apply("playground.hilt")
            }

            // Android Configuration
            extensions.configure<ApplicationExtension> {
                configureAndroid(commonExtension = this)
                defaultConfig.targetSdk = 35
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
            }
        }
    }
}