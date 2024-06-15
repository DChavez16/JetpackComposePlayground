
import androidx.room.gradle.RoomExtension
import com.example.apps.libs
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies


class RoomConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Plugins
            with(pluginManager) {
                // Room
                apply("androidx.room")
                // ksp
                apply("com.google.devtools.ksp")
            }

            // ksp Configuration
            extensions.configure<KspExtension> {
                arg("room.generateKotlin", "true")
            }

            // Room configuration
            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            // Dependencies
            dependencies {
                // Room
                "implementation"(libs.findBundle("room").get())

                // Room ksp
                "ksp"(libs.findLibrary("room.compiler").get())
            }
        }
    }
}