package com.example.recycleview.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recycleview.data.User
import com.example.recycleview.databinding.FragmentUserListBinding
import com.example.recycleview.domain.UserActionListener
import com.example.recycleview.presentation.UsersAdapter
import com.example.recycleview.presentation.factory
import com.example.recycleview.presentation.navigator

class UserListFragment: Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private lateinit var adapter: UsersAdapter

    private val viewModel : UserListViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserListBinding.inflate(inflater,container,false)

        adapter = UsersAdapter(object :UserActionListener{
            override fun onUserMove(user: User, moveBy: Int) {
                viewModel.moveUser(user,moveBy)
            }

            override fun onUserDelete(user: User) {
                viewModel.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                navigator().showDetails(user)
            }

            override fun onFireUse(user: User) {
                viewModel.userFire(user)
            }

        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter


        viewModel.usersLd.observe(viewLifecycleOwner, Observer {
            adapter.users = it
        })


        return binding.root
    }

}