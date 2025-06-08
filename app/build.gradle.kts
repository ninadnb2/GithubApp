plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.ksp)
    id("kotlin-kapt")
}
hilt {
    enableAggregatingTask = false
}

android {
    namespace = "com.example.githubapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.githubapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // Lifecycle + ViewModel + LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Retrofit & Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Glide for image loading
    implementation(libs.glide)
    kapt(libs.compiler) // if using annotation-based config

    // Coroutines (used in ViewModel)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Hilt for Dependency Injection

    implementation(libs.hilt.android.v250)
    kapt(libs.hilt.compiler.v250)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.javapoet)
    kapt(libs.androidx.hilt.compiler)

}