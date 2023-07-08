package com.example.linksdemo.data

import com.google.gson.annotations.SerializedName

data class APIResponse(
    val status : Boolean,
    val statusCode : Int,
    val message : String,
    val support_whatsapp_number: String,
    val today_clicks: Int,
    val top_source: String,
    val top_location: String,
    val startTime: String,

    @SerializedName("data")
    val data: DashboardData
)
