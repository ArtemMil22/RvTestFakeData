package com.example.recycleview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recycleview.databinding.ActivityMainBinding
import com.example.recycleview.model.UserService
import com.example.recycleview.model.UsersListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    private val usersService: UserService
        get() = (applicationContext as App).userService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UsersAdapter()

        val layoutManager = LinearLayoutManager(this)
        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.adapter = adapter

        usersService.addListener(usersListener)
    }

        private val usersListener:UsersListener ={
            adapter.users =it
        }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }
}