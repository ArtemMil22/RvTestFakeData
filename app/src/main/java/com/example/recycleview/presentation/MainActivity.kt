package com.example.recycleview.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.example.recycleview.R
import com.example.recycleview.data.User
import com.example.recycleview.databinding.ActivityMainBinding
import com.example.recycleview.domain.Navigator
import com.example.recycleview.screens.UserDetailsFragment
import com.example.recycleview.screens.UserListFragment

class MainActivity : AppCompatActivity(),Navigator {

    private lateinit var binding: ActivityMainBinding

    private val actions = mutableListOf<() -> Unit>()

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
                .replace(R.id.fragmentContainer, UserDetailsFragment.newInstance(user.id))
                .commit()

    }

    override fun goBack() {
        runWhenActive { onBackPressed() }
    }

    override fun toast(messageRes: Int) {
        Toast.makeText(this,messageRes,Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        actions.forEach { it() }
        actions.clear()
    }

    private fun runWhenActive(action: () -> Unit) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            action()
        } else {
            actions += action
        }
    }
}