plugins {
    `kotlin-dsl`
}

group = "com.example.apps.buildLogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Gradle
    compileOnly(libs.gradle)
    // Kotlin
    compileOnly(libs.kotlin.gradle.plugin)
    // KSP
    compileOnly(libs.ksp.gradle.plugin)
    // Room
    compileOnly(libs.room.gradle.plugin)
}

gradlePlugin {
    // Register convention plugins so they are availabel in the build scripts of the application
    plugins {
        // Android Application Convention Plugin
        register("androidApplication") {
            id = "playground.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        // Android Library Convention Plugin
        register("androidLibrary") {
            id = "playground.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        // Feature Convention Plugin
        register("feature") {
            id = "playground.feature"
            implementationClass = "FeatureConventionPlugin"
        }
        // Hilt Convention Plugin
        register("hilt") {
            id = "playground.hilt"
            implementationClass = "HiltConventionPlugin"
        }
        // Application Compose Convention Plugin
        register("applicationCompose") {
            id = "playground.application.compose"
            implementationClass = "ApplicationComposeConventionPlugin"
        }
        // Library Compose Convention Plugin
        register("libraryCompose") {
            id = "playground.library.compose"
            implementationClass = "LibraryComposeConventionPlugin"
        }
        // Room Convention Plugin
        register("room") {
            id = "playground.room"
            implementationClass = "RoomConventionPlugin"
        }
        // Retrofit Convention Plugin
        register("retrofit") {
            id = "playground.retrofit"
            implementationClass = "RetrofitConventionPlugin"
        }
    }
}
