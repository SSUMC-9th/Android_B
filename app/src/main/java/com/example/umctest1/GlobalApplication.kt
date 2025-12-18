package com.example.umctest1

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.example.umctest1.BuildConfig

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_KEY)
    }
}