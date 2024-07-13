import org.jetbrains.kotlin.gradle.internal.kapt.incremental.UnknownSnapshot.classpath

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()  // For Google's Maven repository
        mavenCentral() // For Bin tray's JCenter repository
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:4.2.0'  // Update to the latest version of Android Gradle Plugin
    }
}

allprojects {
    repositories {
        google()  // Add Google's Maven repository
        mavenCentral() // Add JCenter repository (if needed)
    }
}

task() clean(type: Delete) {
    delete rootProject.buildDir
}
