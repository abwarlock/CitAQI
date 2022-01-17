package com.example.cityaqi.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.cityaqi.R
import com.example.cityaqi.viewmodels.factory.ViewModelFactory
import com.example.cityaqi.viewmodels.models.MainViewModel

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(this))
            .get(MainViewModel::class.java)

        setContentView(R.layout.activity_main)

        connectionLiveData.observe(this) { connected ->
            if (connected) {
                mainViewModel.subscribeToSocketEvents()
            } else {
                mainViewModel.unSubscribeToSocketEvents()
            }
        }
    }
}