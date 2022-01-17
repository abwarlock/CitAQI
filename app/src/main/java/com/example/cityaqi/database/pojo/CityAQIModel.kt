package com.example.cityaqi.database.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cityaqitable",
    indices = [Index(value = ["time"], unique = true)]
)
data class CityAQIModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "time")
    var time: Long,

    @ColumnInfo(name = "data")
    var data: String?
)