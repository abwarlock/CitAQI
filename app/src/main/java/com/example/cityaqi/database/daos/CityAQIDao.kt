package com.example.cityaqi.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cityaqi.database.pojo.CityAQIModel

@Dao
interface CityAQIDao {
    @Query("SELECT * FROM cityaqitable ORDER BY time DESC")
    fun getData(): LiveData<List<CityAQIModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cityAQIModel: CityAQIModel)

    @Query("DELETE FROM cityaqitable")
    suspend fun clear()

    @Query("SELECT time FROM cityaqitable WHERE time = (SELECT MAX(time) FROM cityaqitable) LIMIT 1")
    fun fetchLatestTime(): Long?

    @Query("SELECT * FROM cityaqitable WHERE time = (SELECT MAX(time) FROM cityaqitable)")
    fun fetchLatestUpdate(): LiveData<List<CityAQIModel>>?

    @Query("SELECT * FROM cityaqitable WHERE time = (SELECT MAX(time) FROM cityaqitable)")
    suspend fun fetchLatestData(): List<CityAQIModel>?

    @Query("SELECT * FROM cityaqitable WHERE id = :id")
    suspend fun fetchDataWithID(id: Long): List<CityAQIModel>?
}