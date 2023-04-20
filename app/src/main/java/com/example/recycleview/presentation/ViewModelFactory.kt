package com.example.recycleview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recycleview.screens.UserDetailsViewModel
import com.example.recycleview.screens.UserListViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            UserListViewModel::class.java -> {
                UserListViewModel(app.userService)
            }
            UserDetailsViewModel::class.java -> {
                UserDetailsViewModel(app.userService)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}
