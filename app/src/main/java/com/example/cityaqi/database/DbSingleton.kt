package com.example.cityaqi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cityaqi.database.daos.CityAQIDao
import com.example.cityaqi.database.pojo.CityAQIModel

@Database(entities = [CityAQIModel::class], version = 1)
abstract class DbSingleton : RoomDatabase() {

    abstract fun cityAQIDao(): CityAQIDao

    companion object {
        private var INSTANCE: DbSingleton? = null

        fun getInstance(context: Context): DbSingleton {
            if (INSTANCE == null) {
                INSTANCE = Room
                    .databaseBuilder(context, DbSingleton::class.java, "aqi_table")
                    .build()
            }
            return INSTANCE as DbSingleton
        }
    }
}