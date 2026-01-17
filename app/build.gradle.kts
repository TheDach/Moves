plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.thedach.moves"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.thedach.moves"
        minSdk = 26
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

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")

    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // room
    val room_version = "2.7.2"
    implementation("androidx.room:room-runtime:${room_version}")
    implementation("androidx.room:room-rxjava3:${room_version}")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // RxJava3
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}