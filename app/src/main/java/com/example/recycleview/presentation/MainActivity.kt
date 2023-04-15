package com.example.recycleview.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recycleview.data.User
import com.example.recycleview.databinding.ActivityMainBinding
import com.example.recycleview.data.UserService
import com.example.recycleview.data.UsersListener
import com.example.recycleview.domain.UserActionListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    private val usersService: UserService
        get() = (applicationContext as App).userService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

            //обработчики на действие в контекстном меню
        adapter = UsersAdapter(object : UserActionListener {

            override fun onUserMove(user: User, moveBy: Int) {
                usersService.moveUser(user,moveBy)
            }
            override fun onUserDelete(user: User) {
                usersService.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                Toast.makeText(
                    this@MainActivity,
                    "User: ${user.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.adapter = adapter

        usersService.addListener(usersListener)
    }

    private val usersListener: UsersListener = {
        adapter.users = it
    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }
}