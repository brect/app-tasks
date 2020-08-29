package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val mSharedPreferences = SecurityPreferences(application)
    private val mPersonRepository = PersonRepository(application)

    private val mRegister = MutableLiveData<ValidationListener>()
    var register: LiveData<ValidationListener> = mRegister


    fun register(name: String, email: String, password: String) {
        mPersonRepository.register(name, email, password, object : ApiListener {
            override fun onSuccess(headerModel: HeaderModel) {

                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY, headerModel.token)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY, headerModel.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, headerModel.name)

                mRegister.value = ValidationListener()
            }

            override fun onFailure(msg: String) {
                mRegister.value = ValidationListener(msg)
            }

        })
    }

}