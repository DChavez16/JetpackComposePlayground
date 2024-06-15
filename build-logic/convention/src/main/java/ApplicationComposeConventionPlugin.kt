import com.android.build.api.dsl.ApplicationExtension
import com.example.apps.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


class ApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Plugin
            with(pluginManager) {
                // Android Application
                apply("com.android.application")
            }

            // Configuration
            extensions.configure<ApplicationExtension> {
                configureCompose(this)
            }
        }
    }
}