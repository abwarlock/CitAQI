package com.example.cityaqi.viewmodels.repositorys

import com.example.cityaqi.network.WebServiceProvider

class MainRepository constructor(private val webServiceProvider: WebServiceProvider) {

    fun startSocket() = webServiceProvider.startSocket()

    fun closeSocket() = webServiceProvider.stopSocket()
}