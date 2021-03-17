package com.snail.versionplugin

object Versions {
    const val appcompat = "1.2.0"
    const val coreKtx = "1.3.2"
    const val coroutines = "1.4.2"
    const val material = "1.2.1"
    const val constraintlayout = "2.0.4"
    const val navigation_fragment_ktx = "2.3.2"
    const val navigation_ui_ktx = "2.3.2"
    const val lifecycle_livedata_ktx = "2.2.0"
    const val lifecycle_viewmodel_ktx = "2.2.0"
    const val okhttp = "4.9.0"
    const val retrofit = "2.9.0"
    const val glide = "4.11.0"
    const val room = "2.2.5"
    const val moshi = "1.11.0"
    const val koin = "2.2.1"
    const val kotlin_version = "1.4.30"
}

object Kt{
   const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_version}"
}

object AndroidX {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val androidXCore = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
}

object Coroutines{
    const val coroutines =  "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
}

object Koin{
    const val  koin = "org.koin:koin-android:${Versions.koin}"
    const val scope = "org.koin:koin-androidx-scope:${Versions.koin}"
    const val viewModel =  "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val fragment = "org.koin:koin-androidx-fragment:${Versions.koin}"
}

object Material {
    const val material = "com.google.android.material:material:${Versions.material}"
}

object Navigation {
    const val fragmentKTX =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation_fragment_ktx}"
    const val uiKTX = "androidx.navigation:navigation-ui-ktx:${Versions.navigation_ui_ktx}"
}

object Lifecycle {
    const val liveDataKTX =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle_livedata_ktx}"
    const val viewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_viewmodel_ktx}"
}

object Http {
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moShi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moShiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
}


object Glide {
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideKapt = "com.github.bumptech.glide:compiler:${Versions.glide}"
}

object Room {
    const val room = "androidx.room:room-runtime:${Versions.room}"
    const val roomKapt = "androidx.room:room-compiler:${Versions.room}"
    const val roomKTX = "androidx.room:room-ktx:${Versions.room}"
}

object WebView {
    const val agentWeb = "com.just.agentweb:agentweb-androidx:4.1.4"

}

object Leak {
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.6"
}