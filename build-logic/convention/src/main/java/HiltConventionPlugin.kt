
import com.example.apps.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


class HiltConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Plugin
            with(pluginManager) {
                // Hilt
                apply("com.google.dagger.hilt.android")
                // KSP
                apply("com.google.devtools.ksp")
            }

            // Dependencies
            dependencies {
                // Hilt
                "implementation"(libs.findLibrary("hilt.android").get())
                "ksp"(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}