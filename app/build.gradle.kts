plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.uplift"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.uplift"
        minSdk = 24
        targetSdk = 34
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

    implementation("com.google.firebase:firebase-auth:21.0.8")
    implementation("com.google.firebase:firebase-database:20.1.0")
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation(libs.androidx.core.ktx)
    // Kotlin Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // OkHttp for API calls
    implementation( "com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}