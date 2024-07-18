import com.example.apps.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


class RetrofitConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Dependencies
            dependencies {
                // Retrofit & Kotlin serialization converter
                "implementation"(libs.findBundle("retrofit").get())
            }
        }
    }
}