package com.example.cityaqi.viewmodels.factory

import android.content.Context
import androidx.collection.ArrayMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cityaqi.network.WebServiceProvider
import com.example.cityaqi.viewmodels.intractors.MainInteractor
import com.example.cityaqi.viewmodels.models.ConnectionLiveData
import com.example.cityaqi.viewmodels.models.MainViewModel
import com.example.cityaqi.viewmodels.repositorys.MainRepository


class ViewModelFactory constructor(val context: Context) : ViewModelProvider.Factory {
    private var creators: ArrayMap<Class<*>, ViewModel>? = null

    init {
        creators = ArrayMap()
    }


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var model = creators?.get(modelClass)
        if (model == null) {
            if (modelClass == MainViewModel::class.java) {
                model = MainViewModel(context, MainInteractor(MainRepository(WebServiceProvider())))
                creators?.put(modelClass, model)
            }
        }
        return model as T
    }
}