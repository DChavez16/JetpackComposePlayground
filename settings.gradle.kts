pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "jetpackComposePlayground"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app:auto")
include(":app:tv")
include(":app:wear")
include(":app:mobile")

include(":core:network")
include(":core:database")
include(":core:dataStore")
include(":core:workManager")
include(":core:ui")
include(":core:model")
include(":core:util")
include(":core:testing")

include(":data:product")
include(":data:preferences")
include(":data:notification")
include(":data:theme")
include(":data:notes")
include(":data:userTag")

include(":feature:lazyLayouts")
include(":feature:animations")
include(":feature:drawScope")
include(":feature:themes")
include(":feature:remoteDatabase")
include(":feature:localDatabase")
include(":feature:dataPersistence")
include(":feature:persistentWork")
include(":feature:navigationUi")
include(":feature:configuration")
