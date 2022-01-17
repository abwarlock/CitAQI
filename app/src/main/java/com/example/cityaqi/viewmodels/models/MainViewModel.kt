package com.example.cityaqi.viewmodels.models


import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityaqi.database.DbSingleton
import com.example.cityaqi.database.pojo.CityAQIModel
import com.example.cityaqi.viewmodels.intractors.MainInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.math.abs

class MainViewModel constructor(context: Context, private val intractor: MainInteractor) :
    AndroidViewModel(context.applicationContext as Application) {

    private fun dbSingleton() = DbSingleton.getInstance(getApplication())

    fun unSubscribeToSocketEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                intractor.stopSocket()
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    fun subscribeToSocketEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                intractor.startSocket()
                    .consumeEach {
                        if (it.exception == null) {
                            Timber.d(it.text)
                            val cityAQIDao = dbSingleton().cityAQIDao()
                            val fetchLatestTime = cityAQIDao.fetchLatestTime()
                            val timeNow = Calendar.getInstance().time.time
                            if (fetchLatestTime == null || timeDifferencesMore(
                                    fetchLatestTime,
                                    timeNow
                                )
                            ) {
                                Timber.d("insert data success.")
                                cityAQIDao.insert(CityAQIModel(time = timeNow, data = it.text))
                            } else {
                                Timber.d("insert data skipped.")
                            }
                        } else {
                            onSocketError(it.exception)
                        }
                    }
            } catch (ex: Exception) {
                onSocketError(ex)
            }
        }
    }

    private fun timeDifferencesMore(date: Long, dateNow: Long): Boolean {
        val mills = abs(dateNow - date)
        val secs = ((mills / 1000).toInt() % 60).toLong()
        return secs > 30
    }

    private fun onSocketError(ex: Throwable?) {
        Timber.e(ex)
    }

    override fun onCleared() {
        unSubscribeToSocketEvents()
        super.onCleared()
    }
}