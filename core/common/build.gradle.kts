plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}
apply(from = rootProject.file("config/common-android-library.gradle"))
apply(from = rootProject.file("config/uses-compose.gradle"))

android.namespace = "de.schwarz.it.core.common"

dependencies {
}