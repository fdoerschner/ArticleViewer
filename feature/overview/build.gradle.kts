plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
}

apply(from = rootProject.file("config/common-android-library.gradle"))
apply(from = rootProject.file("config/uses-compose.gradle"))

android.namespace = "de.schwarz.it.feature.overview"

dependencies {
    implementation(project(":data:database"))
    implementation(project(":core:common"))

    implementation(libs.androidx.material3.appcompat)
    implementation(libs.androidx.material3.adaptive)
    implementation(libs.androidx.material3.adaptive.layout)
    implementation(libs.androidx.material3.adaptive.navigation)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)
}