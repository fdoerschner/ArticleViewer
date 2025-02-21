plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
}

apply(from = rootProject.file("config/common-android-library.gradle"))
apply(from = rootProject.file("config/uses-compose.gradle"))

android.namespace = "de.schwarz.it.feature.addArticle"

dependencies {
    implementation(project(":core:common"))
    implementation(project(":data:database"))

    implementation(libs.androidx.material3.appcompat)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)
}