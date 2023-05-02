package com.example.recycleview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recycleview.R
import com.example.recycleview.data.UserDetails
import com.example.recycleview.data.UserService
import com.example.recycleview.tasks.EmptyResult
import com.example.recycleview.tasks.PendingResult
import com.example.recycleview.tasks.Result
import com.example.recycleview.tasks.SuccessResult

class UserDetailsViewModel(
    private val userService: UserService
) : BaseViewModel() {

    private val _stateUserDetails = MutableLiveData<StateUserDetails>()
    val stateUserDetails: LiveData<StateUserDetails> = _stateUserDetails

    private val currentState: StateUserDetails get() = stateUserDetails.value!!

    private val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast: LiveData<Event<Int>> = _actionShowToast

    private val _actionGoBack = MutableLiveData<Event<Unit>>()
    val actionGoBack: LiveData<Event<Unit>> = _actionGoBack

    //изначальное состояние экрана
    init {
        _stateUserDetails.value = StateUserDetails(
            userDetailsResult = EmptyResult(),
            deletingInProgress = false
        )
    }

    fun loadUser(userId: Long) {
        // если пользователь уже загружен то делать ни чего не будем
        if (currentState.userDetailsResult !is EmptyResult) return
        // иначе состояние поменялось
        _stateUserDetails.value = currentState.copy(userDetailsResult = PendingResult())
        userService.getById(userId)
            .onSuccessful {
                _stateUserDetails.value = currentState.copy(userDetailsResult = SuccessResult(it))
            }
            .onError {
                _actionShowToast.value = Event(R.string.cant_load_user_details)
                _actionGoBack.value = Event(Unit)
            }
            .autoCancel()
    }

    fun deleteUser() {
        val userDetailsResult = currentState.userDetailsResult
        if (userDetailsResult !is SuccessResult) return
        _stateUserDetails.value = currentState.copy(deletingInProgress = true)
        userService.deleteUser(userDetailsResult.data.user)
            .onSuccessful {
                _actionShowToast.value = Event(R.string.user_has_been_deleted)
                _actionGoBack.value = Event(Unit)
            }
            .onError {
                _stateUserDetails.value = currentState
                    .copy(deletingInProgress = false)
                _actionShowToast.value = Event(R.string.cant_delete_user)
            }
            .autoCancel()
    }

    //data класс будет содержать в себе данные удобныне
    // для отрисоки на стороне фрагмента
    data class StateUserDetails(
        val userDetailsResult: Result<UserDetails>,
        private val deletingInProgress: Boolean
    ) {

        val showContent: Boolean get() = userDetailsResult is SuccessResult
        val showProgress: Boolean get() = userDetailsResult is PendingResult || deletingInProgress
        val enableDeleteButton: Boolean get() = !deletingInProgress
    }

}