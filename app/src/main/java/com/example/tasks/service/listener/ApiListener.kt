package com.example.tasks.service.listener

import com.example.tasks.service.HeaderModel

interface ApiListener {

    fun onSuccess(headerModel: HeaderModel)
    fun onFailure(msg: String)
}