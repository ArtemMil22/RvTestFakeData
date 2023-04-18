package com.example.recycleview.tasks

sealed class ResultState<T>{

    @Suppress("UNCHECKED_CAST")
    fun <R> map(mapper: (T) -> R): ResultState<R> {
        if (this is SuccessResult) return SuccessResult(mapper(data))
        return this as ResultState<R>
      }
   }

    class SuccessResult<T>(
        val data: T
    ) : ResultState<T>()

    class ErrorResult<T>(
        val error: Throwable
    ) : ResultState<T>()

    class PendingResult<T> : ResultState<T>()

    class EmptyResult<T> : ResultState<T>()
