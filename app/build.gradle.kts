plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myexamapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myexamapp"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation("com.google.firebase:firebase-database:21.0.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")
//    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
//    implementation("com.google.firebase:firebase-auth:23.0.0")
//    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-firestore:25.1.1")
    implementation("com.google.firebase:firebase-storage:21.0.1")
//    implementation("com.google.firebase:firebase-auth:23.1.0")
//    implementation("com.google.firebase:firebase-database:21.0.0")
//    implementation("com.google.firebase:firebase-firestore:25.1.1")
//    implementation("com.google.firebase:firebase-storage:21.0.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.airbnb.android:lottie:3.7.0")
    implementation ("com.github.dhaval2404:imagepicker:2.1")

    implementation ("com.github.mhiew:android-pdf-viewer:3.2.0-beta.3")
}