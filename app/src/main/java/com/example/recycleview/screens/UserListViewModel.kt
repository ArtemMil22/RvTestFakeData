package com.example.recycleview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recycleview.data.User
import com.example.recycleview.data.UserService
import com.example.recycleview.data.UsersListener

class UserListViewModel(
    private val userService: UserService
) : ViewModel() {

    private val usersLdm = MutableLiveData<List<User>>()
    val usersLd: LiveData<List<User>> = usersLdm

    private val listener: UsersListener = {
        usersLdm.value = it
    }

    init {
        loadUsers()
    }

    // дабы избежать утечки памяти
    override fun onCleared() {
        super.onCleared()
        userService.removeListener(listener)
    }

    fun loadUsers() {
        userService.addListener(listener)
    }

    fun moveUser(user: User, moveBy: Int) {
        userService.moveUser(user, moveBy)
    }

    fun deleteUser(user: User) {
        userService.deleteUser(user)
    }
    fun userFire(user: User){
        userService.fireUse(user)
    }

}