plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
}
apply(from = rootProject.file("config/common-android-library.gradle"))

android.namespace = "de.schwarz.it.data.database"

android.defaultConfig {
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.android.compiler)

    api(libs.androidx.room.runtime)

    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.material3.appcompat)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
}