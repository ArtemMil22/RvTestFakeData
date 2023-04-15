package com.example.recycleview

import android.app.Application
import com.example.recycleview.model.UserService

class App:Application() {

    //сделали userService синглтоном, практически везде получить доступ
    val userService = UserService()
}