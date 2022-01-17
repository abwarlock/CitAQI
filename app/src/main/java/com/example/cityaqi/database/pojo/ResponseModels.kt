package com.example.cityaqi.database.pojo

import com.google.gson.annotations.SerializedName

class CityAQIResponse {
    var listValue: ArrayList<CityAQIValue>? = null
}


data class CityAQIValue(
    @SerializedName("city") val city: String?,
    @SerializedName("aqi") val aqi: Float?
)