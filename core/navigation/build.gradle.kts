plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
}
apply(from = rootProject.file("config/common-android-library.gradle"))

android.namespace = "de.schwarz.it.core.navigation"

dependencies {
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
}