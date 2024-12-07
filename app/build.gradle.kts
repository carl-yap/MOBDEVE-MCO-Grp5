plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.mobdeve.s21.manipol.marion.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mobdeve.s21.manipol.marion.myapplication"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    val camerax_version = "1.4.0"
    dependencies {
        implementation("androidx.camera:camera-core:$camerax_version")
        implementation("androidx.camera:camera-camera2:$camerax_version")
        implementation("androidx.camera:camera-lifecycle:$camerax_version")
        implementation("androidx.camera:camera-video:$camerax_version")

        implementation("androidx.camera:camera-view:$camerax_version")
        implementation("androidx.camera:camera-extensions:$camerax_version")

        implementation ("com.google.android.material:material:1.5.0")
    }
    implementation ("com.google.android.material:material:1.5.0")

    implementation ("com.squareup.picasso:picasso:2.8")

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.2.0")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}