package com.example.apps

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


internal fun Project.configureCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        pluginManager.apply {
            apply("org.jetbrains.kotlin.plugin.compose")
        }

        buildFeatures {
            compose = true
        }

//        composeOptions {
//            kotlinCompilerExtensionVersion = libs.findVersion("composeCompiler").get().toString()
//        }

        // Dependencies
        dependencies {
            val bom = libs.findLibrary("compose.bom").get()
            // Compose bom
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))

            // Compose
            "implementation"(libs.findBundle("compose").get())

            // Debug
            "debugImplementation"(libs.findLibrary("compose.ui.tooling").get())
        }
    }
}