package com.example.recycleview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recycleview.R
import com.example.recycleview.data.User
import com.example.recycleview.data.UserListItem
import com.example.recycleview.data.UserService
import com.example.recycleview.data.UsersListener
import com.example.recycleview.domain.UserActionListener
import com.example.recycleview.tasks.*

class UserListViewModel(
    private val userService: UserService
) : BaseViewModel(), UserActionListener {

    private val _users = MutableLiveData<Result<List<UserListItem>>>()
    val users: LiveData<Result<List<UserListItem>>> = _users

    private val _actionShowDetails = MutableLiveData<Event<User>>()
    val actionShowDetails: LiveData<Event<User>> = _actionShowDetails

    private val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast: LiveData<Event<Int>> = _actionShowToast

    //содержит набор идентификаторов которые сейчас в обработке
    // текущее состояние VM
    private val userIdInProgress = mutableSetOf<Long>()
    private var usersResult: Result<List<User>> = EmptyResult()
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
        userService.addListener(listener)
        loadUsers()
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

    override fun onUserMove(user: User, moveBy: Int) {
        if (isInProgress(user)) return
        addProgressTo(user)
        userService.moveUser(user, moveBy)
            .onSuccessful {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
                _actionShowToast.value = Event(R.string.cant_move_user)
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
                _actionShowToast.value = Event(R.string.cant_delete_user)
            }
            .autoCancel()
    }

    override fun onUserDetails(user: User) {
        _actionShowDetails.value = Event(user)
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
        _users.postValue(usersResult.map { users ->
            users.map { user -> UserListItem(user, isInProgress(user)) }
        })
    }

    override fun onFireUse(user: User) {
        userService.fireUse(user)
    }
}