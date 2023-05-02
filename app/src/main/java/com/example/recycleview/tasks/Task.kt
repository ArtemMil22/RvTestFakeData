package com.example.recycleview.tasks

typealias Callback<T> = (T) -> Unit

interface Task<T> {

    fun onSuccessful(callback: Callback<T>): Task<T>
    fun onError(callback: Callback<Throwable>): Task<T>
    fun cancel()
    fun await(): T

}