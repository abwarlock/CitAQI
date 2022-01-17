package com.example.cityaqi.viewmodels.intractors

import com.example.cityaqi.viewmodels.repositorys.MainRepository

class MainInteractor constructor(private val repository: MainRepository) {

    fun stopSocket() = repository.closeSocket()

    fun startSocket() = repository.startSocket()
}