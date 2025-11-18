package com.example.umctest1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TestViewModel : ViewModel() {
    // 공유할 텍스트
    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    // 값 변경 함수
    fun setText(newValue: String) {
        _text.value = newValue
    }
}