package com.example.tasks.service.listener

import com.example.tasks.service.model.HeaderModel

interface ApiListener {

    fun onSuccess(headerModel: HeaderModel)
    fun onFailure(msg: String)
}