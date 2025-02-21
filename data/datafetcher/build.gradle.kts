plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
}
apply(from = rootProject.file("config/common-android-library.gradle"))


android.namespace = "de.schwarz.it.data.datafetcher"
//android {
//    packaging {
//        resources {
//
//            "META-INF/main.kotlin_module",
//            "META-INF/core_release.kotlin_module",
//            "META-INF/kotlin-jupyter-libraries/**",
//            "META-INF/thirdparty-LICENSE",
//            excludes += "META-INF/AL2.0"
//            excludes += "META-INF/LGPL2.1"
//            excludes += "META-INF/LICENSE"
//            excludes += "META-INF/LICENSE.txt"
//            excludes += "META-INF/license.txt"
//            excludes += "META-INF/NOTICE"
//            excludes += "META-INF/NOTICE.txt"
//            excludes += "META-INF/notice.txt"
//            excludes += "META-INF/DEPENDENCIES"
//            excludes += "arrow-git.properties"
//        }
//    }
//}

dependencies {
    ksp(libs.hilt.android.compiler)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.kotlinx.dataframe)
    implementation(libs.kotlinx.serialization.json)
}