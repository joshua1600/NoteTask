plugins {
    id("com.android.application")
}

android {
    namespace = "my.edu.utar.notetask"
    compileSdk = 34

    defaultConfig {
        applicationId = "my.edu.utar.notetask"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

//    //Room (for database)
//    implementation ("androidx.room:room-runtime:2.2.5")
//    annotationProcessor ("androidx.room:room-compiler:2.2.5")
//
//    //RecyclerView
//    implementation ("androidx. recyclerview:recyclerview:1.1.0")
//    // Scalable Size Unit (support for different screen sizes)
//    implementation ("com.intuit.sdp:sdp-android:1.0.6")
//    implementation ("com.intuit.ssp:ssp-android:1.0.6")
//    // Material Design
//    implementation ("com-google.android material:material:1.1.0")
//    // Rounded ImageView
//    implementation ("com-makeramen: roundedimageview:2.3.0")
}