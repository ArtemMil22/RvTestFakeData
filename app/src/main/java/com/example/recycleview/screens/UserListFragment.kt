package com.example.recycleview.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recycleview.data.User
import com.example.recycleview.databinding.FragmentUserListBinding
import com.example.recycleview.domain.UserActionListener
import com.example.recycleview.domain.factory
import com.example.recycleview.domain.navigator
import com.example.recycleview.presentation.UsersAdapter
import com.example.recycleview.tasks.EmptyResult
import com.example.recycleview.tasks.ErrorResult
import com.example.recycleview.tasks.PendingResult
import com.example.recycleview.tasks.SuccessResult

class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private lateinit var adapter: UsersAdapter

    private val viewModel: UserListViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserListBinding.inflate(inflater, container, false)

        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                viewModel.onUserMove(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                viewModel.onUserDelete(user)
            }

            override fun onUserDetails(user: User) {
                navigator().showDetails(user)
            }

            override fun onFireUse(user: User) {
                viewModel.onFireUse(user)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        viewModel.users.observe(viewLifecycleOwner) {
            hideAll()
            when (it) {
                is SuccessResult -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    adapter.users = it.data
                }
                is ErrorResult -> {
                    binding.tryAgainContainer.visibility = View.VISIBLE
                }
                is PendingResult -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is EmptyResult -> {
                    binding.noUsersTextView.visibility = View.VISIBLE
                }
            }
        }

        viewModel.actionShowDetails.observe(viewLifecycleOwner) {
            it.getValue()?.let { user -> navigator().showDetails(user) }
        }
        viewModel.actionShowToast.observe(viewLifecycleOwner) {
            it.getValue()?.let { messageRes -> navigator().toast(messageRes) }
        }
        return binding.root
    }

    private fun hideAll() {
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.tryAgainContainer.visibility = View.GONE
        binding.noUsersTextView.visibility = View.GONE
    }
}