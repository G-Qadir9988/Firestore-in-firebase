plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.lab11"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.lab11"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    // ... other dependencies
    implementation("com.google.firebase:firebase-auth:22.3.0")
// Optional: Material design components for styling
    implementation("com.google.android.material:material:1.11.0")


    implementation("com.google.firebase:firebase-database:20.3.0") // ADD THIS LINE
    implementation("com.google.firebase:firebase-analytics:21.5.0")
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    // for firestore
    implementation("com.google.firebase:firebase-firestore:24.11.0")

}