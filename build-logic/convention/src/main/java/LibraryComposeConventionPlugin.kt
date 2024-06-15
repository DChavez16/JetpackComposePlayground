import com.android.build.api.dsl.LibraryExtension
import com.example.apps.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


class LibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Plugin
            with(pluginManager) {
                // Android Library
                apply("com.android.library")
            }

            // Configuration
            extensions.configure<LibraryExtension> {
                configureCompose(this)
            }
        }
    }
}