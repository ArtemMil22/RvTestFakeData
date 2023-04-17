package com.example.recycleview.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recycleview.R
import com.example.recycleview.data.User
import com.example.recycleview.databinding.ActivityMainBinding
import com.example.recycleview.data.UserService
import com.example.recycleview.data.UsersListener
import com.example.recycleview.domain.Navigator
import com.example.recycleview.domain.UserActionListener
import com.example.recycleview.screens.UserDetailsFragment
import com.example.recycleview.screens.UserListFragment

class MainActivity : AppCompatActivity(),Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, UserListFragment())
                .commit()
        }
    }

    override fun showDetails(user: User) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer,UserDetailsFragment.newInstance(user.id))
            .commit()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun toast(messageRes: Int) {
        Toast.makeText(this,messageRes,Toast.LENGTH_SHORT).show()
    }
}