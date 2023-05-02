package com.example.recycleview.presentation

import android.app.Application
import com.example.recycleview.data.UserService

class App:Application() {

    val userService = UserService()

}