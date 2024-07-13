@file:Suppress("DEPRECATION")

val <T> v141: T
    get() {}



val constraintlayout: Any
    get() {}



// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.bundles)
        // Kotlin plugin
        classpath(libs.kotlin.gradle.plugin)
    }
}

plugins {
    id("com.android.application")
    kotlin("android")
}

repositories {
    google()
    mavenCentral()
}

android {
    compileSdkVersion(34)
    buildToolsVersion("31.0.0")

    defaultConfig {
        applicationId = "com.cns.Postnatal application"
        minSdkVersion(21)
        //noinspection ExpiredTargetSdkVersion
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(v141)
    implementation(libs.material)
    implementation(constraintlayout)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.kotlin.stdlib)
    // Add other dependencies as needed
}

// Specify your Kotlin version
val kotlinVersion = "1.6.10"

// Specify your build tools version
val androidBuildToolsVersion = "31.0.0"
