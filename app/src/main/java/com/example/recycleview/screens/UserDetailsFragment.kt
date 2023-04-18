package com.example.recycleview.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.recycleview.R
import com.example.recycleview.databinding.FragmentUserDetailsBinding
import com.example.recycleview.presentation.factory
import com.example.recycleview.presentation.navigator

class UserDetailsFragment: Fragment(){

    private lateinit var binding: FragmentUserDetailsBinding
    private val viewModel: UserDetailsViewModel by viewModels{ factory()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // сообщаем вьюмодели какого пользователя мы хотим загрузить
        viewModel.loadUser(requireArguments().getLong(ARG_USER_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailsBinding.inflate(layoutInflater,container,false)

        viewModel.stateUserDetails.observe(viewLifecycleOwner, Observer {
            binding.userNameTextView.text = it.user.name
            if(it.user.photo.isNotBlank()){
                Glide.with(this)
                    .load(it.user.photo)
                    .circleCrop()
                    .into(binding.photoImageView)
            } else{
                Glide.with(this)
                    .load(R.drawable.ic_user_avatar)
                    .into(binding.photoImageView)
            }
            binding.userDetailsTextView.text = it.details
        })

        binding.deleteButton.setOnClickListener{
            viewModel.deleteUser()
            navigator().toast(R.string.user_has_been_deleted)
            navigator().goBack()
        }

        return binding.root
    }


    companion object{
        private const val ARG_USER_ID = "ARG_USER_ID"

        fun newInstance(userId:Long):UserDetailsFragment{
            val fragment = UserDetailsFragment()
            fragment.arguments = bundleOf(ARG_USER_ID to userId)
            return fragment
        }
    }
}