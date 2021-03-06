package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_KEY
import com.example.tasks.service.constants.TaskConstants.SHARED.TOKEN_KEY
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_NAME
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mSharedPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<ValidationListener>()
    var login: LiveData<ValidationListener> = mLogin

    private val mLoggedUser = MutableLiveData<Boolean>()
    var loggedUser: LiveData<Boolean> = mLoggedUser

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        mPersonRepository.auth(email, password, object : ApiListener{
            override fun onSuccess(headerModel: HeaderModel) {

                mSharedPreferences.store(TOKEN_KEY, headerModel.token)
                mSharedPreferences.store(PERSON_KEY, headerModel.personKey)
                mSharedPreferences.store(PERSON_NAME, headerModel.name)

                mLogin.value = ValidationListener()

            }

            override fun onFailure(msg: String) {
                mLogin.value = ValidationListener(msg)
            }

        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {

       val token = mSharedPreferences.get(TOKEN_KEY)
       val person = mSharedPreferences.get(PERSON_KEY)

        val logged = (token != "" && person != "")
        mLoggedUser.value = logged
    }

}