@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "hansung.ac.kr.androidprogrammingproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "hansung.ac.kr.androidprogrammingproject"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    gradle



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
    // 뷰 바인딩
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Firebase 의존성 추가
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    // RecyclerView 의존성 추가
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    // BottomNavigation 의존성 추가

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}