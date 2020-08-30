package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository = TaskRepository(application)

    private val mTaskList = MutableLiveData<List<TaskModel>>()
    var tasks: LiveData<List<TaskModel>> = mTaskList

    private val mValidationListener = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidationListener

    fun list(){
        mTaskRepository.getAll(object : ApiListener<List<TaskModel>>{
            override fun onSuccess(param: List<TaskModel>) {
                mTaskList.value = param
            }

            override fun onFailure(msg: String) {
                mTaskList.value = arrayListOf()
            }

        })
    }

    fun complete(id: Int){
        updateStatus(id, true)
    }

    fun undo(id: Int){
        updateStatus(id, false)
    }

    private fun updateStatus(id: Int, complete: Boolean){
        mTaskRepository.updateStatus(id, complete, object : ApiListener<Boolean>{
            override fun onSuccess(param: Boolean) {
                list()
                mValidationListener.value = ValidationListener()
            }

            override fun onFailure(msg: String) {
                mValidationListener.value = ValidationListener(msg)
            }

        })
    }

    fun delete(id: Int) {
        mTaskRepository.delete(id, object : ApiListener<Boolean>{
            override fun onSuccess(param: Boolean) {
                list()
                mValidationListener.value = ValidationListener()
            }

            override fun onFailure(msg: String) {
                mValidationListener.value = ValidationListener(msg)
            }

        })
    }


}