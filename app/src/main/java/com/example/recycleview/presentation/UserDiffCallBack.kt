package com.example.recycleview.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.recycleview.data.User
import com.example.recycleview.data.UserListItem

class UserDiffCallBack(
    private val oldList: List<UserListItem>,
    private val newList: List<UserListItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.user.id == newUser.user.id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return  oldUser == newUser
    }
}