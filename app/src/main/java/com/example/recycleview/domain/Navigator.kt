package com.example.recycleview.domain

import com.example.recycleview.data.User

interface Navigator {

    fun showDetails(user: User)

    fun goBack()

    fun toast(messageRes: Int)

}