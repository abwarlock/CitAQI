package com.example.cityaqi.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cityaqi.viewmodels.models.ConnectionLiveData

abstract class BaseActivity : AppCompatActivity() {

    lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        connectionLiveData = ConnectionLiveData(this)
        super.onCreate(savedInstanceState)
    }
}