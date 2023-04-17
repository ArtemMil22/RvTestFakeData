package com.example.recycleview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recycleview.data.UserDetails
import com.example.recycleview.data.UserService
import com.example.recycleview.domain.UserNotFoundException

class UserDetailsViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _userDetails = MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> = _userDetails

    fun loadUser(userId: Long) {
        // если пользователь уже загружен то делать ни чего не будем
        if (_userDetails.value != null) return
        try {
            _userDetails.value = userService.getById(userId)
        } catch (e: UserNotFoundException) {
            e.printStackTrace()
        }
    }

    fun deleteUser() {
        //проверяем есть ли этот пользователь
        val userDetails = this.userDetails.value ?: return

        userService.deleteUser(userDetails.user)

    }

}