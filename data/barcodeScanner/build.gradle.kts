plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

apply(from = rootProject.file("config/common-android-library.gradle"))

android.namespace = "de.schwarz.it.data.barcodescanner"

dependencies {
    implementation(libs.androidx.material3.appcompat)
}