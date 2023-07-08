package com.example.linksdemo.ui.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linksdemo.API.RetroFitClient
import com.example.linksdemo.Adapter.ListAdapter
import com.example.linksdemo.data.Data
import com.example.linksdemo.data.Link
import com.example.linksdemo.databinding.FragmentHomeBinding
import com.example.linksdemo.model.Repository
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var lineChart : LineChart

    private lateinit var topLink : TextView

    private lateinit var recentLink : TextView

    private lateinit var listLinkView : RecyclerView

    private lateinit var select : TextView

    private lateinit var def : ColorStateList

    private  var adapter = ListAdapter(emptyList())

    private  var listTopLinks : List<Link> = emptyList()

    private  var listRecentLinks : List<Link> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val apiservice = RetroFitClient.create()
        val repository = Repository(apiservice)

        val homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        topLink = binding.customTab.topLinks

        recentLink = binding.customTab.recentLink

        select = binding.customTab.select

        listLinkView = binding.recyclerView


        listLinkView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter
        }


        lineChart = binding.lineChart

        lineChart.setDrawGridBackground(true)
        lineChart.description.isEnabled = false

        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)

        val xAxis = lineChart.xAxis

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(true)
        xAxis.valueFormatter = IndexAxisValueFormatter()

        xAxis.granularity = 1f

        lineChart.axisLeft.setDrawGridLines(true)

        lineChart.legend.isEnabled = false


        //Set the default color state list for the text views
        def = recentLink.textColors

        topLink.setOnClickListener{ selectItem(topLink, 0) }
        recentLink.setOnClickListener{ selectItem(recentLink, 1) }




        homeViewModel.APIResponse.observe(viewLifecycleOwner
        ) { APIResponse ->
            listTopLinks = APIResponse.data.topLink
            adapter = ListAdapter(listTopLinks)
            listLinkView.adapter = adapter

        }

        homeViewModel.chartData.observe(viewLifecycleOwner) { linkList ->
            updateChart(linkList)
        }

        homeViewModel.topLinks.observe(viewLifecycleOwner){
            listTopLinks = it
        }

        homeViewModel.recentLinks.observe(viewLifecycleOwner){
            listRecentLinks = it
        }

        homeViewModel.fetchData()

        val greetings: TextView = binding.greeting
        homeViewModel.greeting.observe(viewLifecycleOwner) { greeting ->
            greetings.text = greeting
        }
        val todayClick: TextView = binding.txtTodayClicks
        homeViewModel.todayClicks.observe(viewLifecycleOwner) { todayClicks ->
            todayClick.text = todayClicks.toString()
        }
        val topLocation: TextView = binding.txtTopLocation
        homeViewModel.topLocation.observe(viewLifecycleOwner) {
            topLocation.text = it
        }
        val topSource: TextView = binding.txtTopSource
        homeViewModel.topSource.observe(viewLifecycleOwner) {
            topSource.text = it
        }
        val bestTime: TextView = binding.txtBestTime
        homeViewModel.bestTime.observe(viewLifecycleOwner) {
            bestTime.text = it
        }
        return root
    }



    private fun updateChart(chartList: List<Data>) {
        val entries = mutableListOf<Entry>()
        val xAxisLabels = mutableListOf<String>()


        val leftAxis = lineChart.axisLeft
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.setDrawAxisLine(true)
        leftAxis.setDrawLabels(true)

        val rightAxis = lineChart.axisRight
        rightAxis.isEnabled = false

        for ((index, link) in chartList.withIndex()) {
                val entry = Entry(index.toFloat(), link.totalClicks.toFloat())
                entries.add(entry)

                val formattedDate = SimpleDateFormat("MM-dd", Locale.getDefault()).format(link.date)
                xAxisLabels.add(formattedDate)
        }

        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        xAxis.labelCount = xAxisLabels.size
        xAxis.textSize = 5f
        xAxis.labelRotationAngle = -45f

        val dataSet = LineDataSet(entries, "Total Clicks")
        dataSet.color = Color.BLUE
        dataSet.setDrawCircles(false)

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        lineChart.invalidate()

    }

    private fun selectItem(view: TextView, position: Int) {

        val size = recentLink.width
        when(position){
            0 -> {
                select.animate().x(0f).setDuration(100)
                topLink.setTextColor(Color.WHITE)
                recentLink.setTextColor(def)
                adapter = ListAdapter( listTopLinks)
                listLinkView.adapter = adapter
            }
            1 -> {
                topLink.setTextColor(def)
                recentLink.setTextColor(Color.WHITE)
                select.animate().x(size.toFloat()).setDuration(100)
                adapter = ListAdapter( listRecentLinks)
                listLinkView.adapter = adapter
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}