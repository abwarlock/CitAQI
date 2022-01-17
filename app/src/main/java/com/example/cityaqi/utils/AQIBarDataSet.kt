package com.example.cityaqi.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.cityaqi.R
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class AQIBarDataSet(
    private val context: Context,
    valueList: MutableList<BarEntry>?,
    label: String?
) : BarDataSet(valueList, label) {

    init {
        setColors(
            intArrayOf(
                ContextCompat.getColor(context, R.color.good),
                ContextCompat.getColor(context, R.color.satisfactory),
                ContextCompat.getColor(context, R.color.moderate),
                ContextCompat.getColor(context, R.color.poor),
                ContextCompat.getColor(context, R.color.vpoor),
                ContextCompat.getColor(context, R.color.severe)
            )
        )
    }

    override fun getEntryIndex(e: BarEntry?): Int {
        return yVals.indexOf(e)
    }

    override fun getValueFormatter(): ValueFormatter {
        return ValueFormatter { value, _, _, _ ->
            val decimalFormat = DecimalFormat("###,###,###,##01")
            when {
                value < 50.0 -> {
                    context.getString(R.string.str_aqi_good)
                }
                value < 100.1 -> {
                    context.getString(R.string.str_aqi_satisfactory)
                }
                value < 200.1 -> {
                    context.getString(R.string.str_aqi_modrate)
                }
                value < 300.1 -> {
                    context.getString(R.string.str_aqi_poor)
                }
                value < 400.1 -> {
                    context.getString(R.string.str_aqi_vpoor)
                }
                else -> {
                    context.getString(R.string.str_aqi_severe)
                }
            } + " (${decimalFormat.format(value)})"
        }
    }

    override fun getColor(index: Int): Int {
        return when {
            getEntryForXIndex(index).getVal() < 50.1 -> {
                mColors[0]
            }
            getEntryForXIndex(index).getVal() < 100.1 -> {
                mColors[1]
            }
            getEntryForXIndex(index).getVal() < 200.1 -> {
                mColors[2]
            }
            getEntryForXIndex(index).getVal() < 300.1 -> {
                mColors[3]
            }
            getEntryForXIndex(index).getVal() < 400.1 -> {
                mColors[4]
            }
            else -> {
                mColors[5]
            }
        }
    }


}