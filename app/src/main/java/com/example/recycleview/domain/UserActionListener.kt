package com.example.recycleview.domain

import com.example.recycleview.data.User

// для действий пользователя над списком, для конструктора адаптера
interface UserActionListener {

    fun onUserMove(user: User, moveBy: Int)

    fun onUserDelete(user: User)

    //когда юзер просто нажимает на элемент из списка
    fun onUserDetails(user: User)

    fun onFireUse(user: User)

}