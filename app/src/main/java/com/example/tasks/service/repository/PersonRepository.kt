package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants.HTTP.SUCCESS
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.repository.remote.PersonService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(val context: Context) {

    private var mRemote = RetrofitClient.createService(PersonService::class.java)

    fun auth(email: String, password: String, listener: ApiListener) {

        val call: Call<HeaderModel> = mRemote.auth(email, password)
        call.enqueue(object : Callback<HeaderModel> {
            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {
                if (response.code() != SUCCESS) {
                    val validation =
                        Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }
        })

    }
    fun register(name: String, email: String, password: String, listener: ApiListener) {

        val call: Call<HeaderModel> = mRemote.register(name, email, password)
        call.enqueue(object : Callback<HeaderModel> {
            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {
                if (response.code() != SUCCESS) {
                    val validation =
                        Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }
        })

    }
}