package com.example.linksdemo.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linksdemo.data.APIResponse
import com.example.linksdemo.data.Data
import com.example.linksdemo.data.Link
import com.example.linksdemo.model.Repository
import com.example.linksdemo.data.Result
import java.util.Calendar

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _APIResponse = MutableLiveData<APIResponse>()
    val APIResponse : LiveData<APIResponse> = _APIResponse

    private val _chartData = MutableLiveData<List<Data>>()
    val chartData : LiveData<List<Data>> = _chartData

    private val _recentLinks = MutableLiveData<List<Link>>()
    val recentLinks : LiveData<List<Link>> = _recentLinks

    private val _topLinks = MutableLiveData<List<Link>>()
    val topLinks : LiveData<List<Link>> = _topLinks

    private val _todayClicks = MutableLiveData<Int>().apply {
        value = 123
    }
    val todayClicks: LiveData<Int> = _todayClicks

    private val _topLocation = MutableLiveData<String>().apply {
        value = ""
    }
    val topLocation: LiveData<String> = _topLocation

    private val _topSource = MutableLiveData<String>().apply {
        value = ""
    }
    val topSource: LiveData<String> = _topSource

    private val _bestTime = MutableLiveData<String>().apply {
        value = ""
    }
    val bestTime: LiveData<String> = _bestTime


    private val currentHour: Int
        get() = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)


    fun fetchData() {
        repository.getDashboardData { result ->
            when(result) {
                is Result.Success ->{
                    _APIResponse.value = result.data!!
                    val todayClick = result.data.today_clicks
                    val topLocation = result.data.top_location
                    val topSource = result.data.top_source
                    val bestTime = result.data.startTime
                    val recentLinkList = result.data.data.recentLink
                    val topLinkList = result.data.data.topLink
                    val responseData = result.data.data.overallUrlChart ?: emptyMap()
                    val linkList = responseData.map { (date, clicks) ->
                        Data(date, clicks)
                    }
                    _todayClicks.value = todayClick
                    _topLocation.value = topLocation
                    _topSource.value = topSource
                    _bestTime.value = bestTime
                    _recentLinks.value = recentLinkList
                    _topLinks.value = topLinkList
                    _chartData.value = linkList
                }
                is Result.Error -> handleApiError(result.errorMessage)
            }
        }
    }

    private fun handleApiError(errorMessage: String) {
        Log.d("HomeViewModel",errorMessage)
    }

    private val _greeting = MutableLiveData<String>().apply {
        value = if (currentHour < 12) {
            "Good Morning!"
        } else if (currentHour in 13..17) {
            "Good Afternoon!"
        } else {
            "Good Evening!"
        }
    }
    val greeting: LiveData<String> = _greeting
}