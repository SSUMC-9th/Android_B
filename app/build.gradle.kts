plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "1.9.22"
    id("org.jetbrains.kotlin.kapt") version "1.9.22"
}
//C:\Users\LG\AppData\Local\Android\Sdk
android {
    namespace = "com.example.umc_9th"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.umc_9th"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Compose Compiler 버전
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {
    // 💡 Jetpack Compose BOM (버전만 지정하면 자동으로 맞춰줌)
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("me.relex:circleindicator:2.1.6")


// OkHttp (Retrofit 내부 HTTP 통신)
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
// AndroidX 버전
    // ✅ Compose 핵심 라이브러리들 (런타임 포함)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime")

    // 디버깅용 Compose 툴링
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Glide (★★ 빠져 있어서 오류 발생)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // ✅ 기존 라이브러리들
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-compose:1.8.0") // compose와 호환되는 Activity
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    val roomVersion = "2.6.1"
    //implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-compiler:$roomVersion")
    // 테스트
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
