package com.example.recycleview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recycleview.data.User
import com.example.recycleview.data.UserListItem
import com.example.recycleview.data.UserService
import com.example.recycleview.data.UsersListener
import com.example.recycleview.domain.UserActionListener
import com.example.recycleview.tasks.*

class UserListViewModel(
    private val userService: UserService
) : BaseViewModel(),UserActionListener {

    private val usersLdm = MutableLiveData<ResultState<List<UserListItem>>>()
    val usersLd: LiveData<ResultState<List<UserListItem>>> = usersLdm

    private val _actionShowDetails = MutableLiveData<Event<User>>()
    val actionShowDetails: LiveData<Event<User>> = _actionShowDetails

    //содержит набор идентификаторов которые сейчас в обработке
    // текущее состояние VM
    private val userIdInProgress = mutableSetOf<Long>()
    private var usersResult: ResultState<List<User>> = EmptyResult()
        set(value) {
            field = value
            notifyUpdates()
        }

    private val listener: UsersListener = {
        usersResult = if (it.isEmpty()) {
            EmptyResult()
        } else {
            SuccessResult(it)
        }
    }

    init {
        loadUsers()
        userService.addListener(listener)
    }

    // дабы избежать утечки памяти
    override fun onCleared() {
        super.onCleared()
        userService.removeListener(listener)
    }

    fun loadUsers() {
        usersResult = PendingResult()
        userService.loadUsers()
            .onError {
                usersResult = ErrorResult(it)
            }
            .autoCancel()
    }

    fun moveUser(user: User, moveBy: Int) {

    }

    fun deleteUser(user: User) {

    }

    fun userFire(user: User) {

    }

    private fun addProgressTo(user: User) {
        userIdInProgress.add(user.id)
        notifyUpdates()
    }

    private fun removeProgressFrom(user: User) {
        userIdInProgress.remove(user.id)
        notifyUpdates()
    }

    private fun isInProgress(user: User): Boolean {
        return userIdInProgress.contains(user.id)
    }

    private fun notifyUpdates() {
        usersLdm.postValue(usersResult.map { usersLd ->
            usersLd.map { user: User -> UserListItem(user, isInProgress(user)) }
        })
    }

    fun showDetails(user: User) {

    }

    override fun onUserMove(user: User, moveBy: Int) {
        if (isInProgress(user)) return
        addProgressTo(user)
        userService.moveUser(user, moveBy)
            .onSuccessful {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
                //todo
            }
            .autoCancel()
    }

    override fun onUserDelete(user: User) {
        if (isInProgress(user)) return
        addProgressTo(user)
        userService.deleteUser(user)
            .onSuccessful {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
                //todo
            }
            .autoCancel()
    }

    override fun onUserDetails(user: User) {
        _actionShowDetails.value = Event(user)
    }

    override fun onFireUse(user: User) {
        userService.fireUse(user)
    }
}