package com.example.cityaqi.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceUtil {

    private fun getPref(context: Context): SharedPreferences? {
        return context.getSharedPreferences("PREF", Context.MODE_PRIVATE)
    }

    fun setAutoRefresh(context: Context,value:Boolean) {
        getPref(context)?.edit()?.putBoolean("AUTO_REFRESH", value)?.apply()
    }

    fun fetchAutoRefresh(context: Context): Boolean {
        return getPref(context)?.getBoolean("AUTO_REFRESH", true) ?: true
    }
}