package com.example.linksdemo.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.linksdemo.API.Apiservice
import com.example.linksdemo.data.APIResponse
import com.example.linksdemo.data.Link
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.linksdemo.data.Result


class Repository(private val apiservice: Apiservice) {


    fun getDashboardData(callback: (com.example.linksdemo.data.Result<APIResponse>) -> Unit) {
        val call = apiservice.getDashboardData()
        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()!!
                    data?.let {
                        callback(Result.Success(it))
                    } ?: callback(Result.Error("Response is null"))
                } else {
                    callback(Result.Error("API request failed"))
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                callback(Result.Error(t.message ?: "API request failed"))
            }

        })
    }
}