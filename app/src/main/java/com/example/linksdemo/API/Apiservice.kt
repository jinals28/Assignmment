package com.example.linksdemo.API

import com.example.linksdemo.data.APIResponse
import com.example.linksdemo.data.DashboardData
import retrofit2.Call
import retrofit2.http.GET

interface Apiservice {

    @GET("dashboardNew")
    fun getDashboardData() : Call<APIResponse>
}