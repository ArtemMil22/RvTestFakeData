package com.example.recycleview.screens

import androidx.lifecycle.ViewModel
import com.example.recycleview.tasks.Task

open class BaseViewModel: ViewModel() {

    private val tasks = mutableListOf<Task<*>>()

    override fun onCleared() {
        super.onCleared()
        tasks.forEach { it.cancel() }
    }

    fun <T> Task<T>.autoCancel(){
        tasks.add(this)
    }
}