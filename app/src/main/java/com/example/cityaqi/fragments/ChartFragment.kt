package com.example.cityaqi.fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cityaqi.R
import com.example.cityaqi.database.DbSingleton
import com.example.cityaqi.database.pojo.CityAQIModel
import com.example.cityaqi.utils.AQIBarDataSet
import com.example.cityaqi.utils.PreferenceUtil
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChartFragment : Fragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {}
    }

    val dateFormat = SimpleDateFormat("yyyy/MM/dd   HH:mm:ss", Locale.getDefault())
    var chart: BarChart? = null
    var buttonPanel: RelativeLayout? = null

    val dataSet = lazy {
        DbSingleton.getInstance(requireContext()).cityAQIDao().fetchLatestUpdate()
    }

    val observer =
        androidx.lifecycle.Observer<List<CityAQIModel>> { list -> list?.let { updateData(it) } }

    var cityAQIModel: CityAQIModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_chart, container, false)
        chart = rootView.findViewById(R.id.barChart)
        buttonPanel = rootView.findViewById(R.id.buttonPanel)

        (rootView.findViewById(R.id.prev) as Button?)?.setOnClickListener(this)
        (rootView.findViewById(R.id.next) as Button?)?.setOnClickListener(this)

        chart?.setDrawGridBackground(false)
        chart?.setDrawValueAboveBar(true)
        chart?.setDrawHighlightArrow(true)
        chart?.setDescription("")

        chart?.legend?.isEnabled = false

        updateAutoUpdate()

        return rootView
    }

    private fun updateAutoUpdate() {
        updateButtonPanel()
        if (PreferenceUtil.fetchAutoRefresh(requireContext())) {
            dataSet.value?.observe(viewLifecycleOwner, observer)
        } else {
            dataSet.value?.removeObserver(observer)
            GlobalScope.launch(Dispatchers.Main) {
                val listData = DbSingleton.getInstance(requireContext())
                    .cityAQIDao()
                    .fetchLatestData()
                listData?.let { updateData(it) }
            }
        }
    }

    private fun updateData(list: List<CityAQIModel>) {
        if (list.isEmpty()) {
            return
        }
        cityAQIModel = list.first()

        cityAQIModel?.let {
            updateTitle(it)
            val cityAQIResponse = JSONArray(it.data)

            val listAQI = ArrayList<BarEntry>()
            val listCity = ArrayList<String>()

            for (i in 0 until cityAQIResponse.length()) {
                val model = cityAQIResponse.optJSONObject(i)
                listAQI.add(BarEntry(model.optDouble("aqi", 0.0).toFloat(), i))
                listCity.add(model.optString("city", ""))
            }

            val barDataSet = AQIBarDataSet(requireContext(), listAQI, getString(R.string.str_aqi))
            chart?.animateY(3000)
            chart?.data = BarData(listCity, barDataSet)

            chart?.invalidate()
        }
    }

    private fun updateButtonPanel() {
        val autoRefresh = PreferenceUtil.fetchAutoRefresh(requireContext())
        buttonPanel?.let {
            it.visibility = if (autoRefresh) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_menu, menu)
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.itemId == R.id.disable_refresh) {
                item.isVisible = PreferenceUtil.fetchAutoRefresh(requireContext())
            } else if (item.itemId == R.id.enable_refresh) {
                item.isVisible = !PreferenceUtil.fetchAutoRefresh(requireContext())
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.disable_refresh -> {
                PreferenceUtil.setAutoRefresh(requireContext(), false)
            }
            R.id.enable_refresh -> {
                PreferenceUtil.setAutoRefresh(requireContext(), true)
            }
        }
        updateAutoUpdate()
        activity?.invalidateOptionsMenu()
        return false
    }

    private fun updateTitle(cityAQIModel: CityAQIModel) {
        activity?.title = getString(R.string.str_aqi)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.subtitle = "Fetched At - ${
                dateFormat.format(
                    Date(
                        cityAQIModel.time
                    )
                )
            }"
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.prev -> {
                fetchDataWithId(cityAQIModel?.id?.minus(1))
            }
            R.id.next -> {
                fetchDataWithId(cityAQIModel?.id?.plus(1))
            }
        }
    }

    private fun fetchDataWithId(id: Long?) {
        GlobalScope.launch(Dispatchers.Main) {
            val listData = id?.let {
                DbSingleton.getInstance(requireContext())
                    .cityAQIDao()
                    .fetchDataWithID(it)
            }
            if(listData?.isEmpty() == true){
                Toast.makeText(requireContext(),"No data Available",Toast.LENGTH_SHORT).show()
            }else{
                listData?.let { updateData(it) }
            }
        }
    }

}