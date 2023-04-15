package com.example.recycleview.presentation

import android.app.Application
import com.example.recycleview.data.UserService

class App:Application() {

    //сделали userService синглтоном, практически везде получаем доступ к нему
    val userService = UserService()
}