package com.example.recycleview.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.recycleview.R
import com.example.recycleview.databinding.FragmentUserDetailsBinding
import com.example.recycleview.domain.factory
import com.example.recycleview.domain.navigator
import com.example.recycleview.tasks.SuccessResult

class UserDetailsFragment : Fragment() {

    private lateinit var binding: FragmentUserDetailsBinding
    private val viewModel: UserDetailsViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // сообщаем вьюмодели какого пользователя мы хотим загрузить
        viewModel.loadUser(requireArguments().getLong(ARG_USER_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailsBinding.inflate(
            layoutInflater, container, false
        )

        viewModel.actionShowToast.observe(viewLifecycleOwner) {
            it.getValue()?.let { messageRes -> navigator().toast(messageRes) }
        }
        viewModel.actionGoBack.observe(viewLifecycleOwner) {
            it.getValue()?.let { navigator().goBack() }
        }

        viewModel.stateUserDetails.observe(viewLifecycleOwner) {
            // контент будем показывать только тогда когда результат успешен
            binding.contentContainer.visibility = if (it.showContent) {

                val userDetails = (it.userDetailsResult as SuccessResult).data
                binding.userNameTextView.text = userDetails.user.name

                if (userDetails.user.photo.isNotBlank()) {
                    Glide.with(this)
                        .load(userDetails.user.photo)
                        .circleCrop()
                        .into(binding.photoImageView)
                } else {
                    Glide.with(this)
                        .load(R.drawable.ic_user_avatar)
                        .into(binding.photoImageView)
                }
                binding.userDetailsTextView.text = userDetails.details
                View.VISIBLE

            } else {
                View.GONE
            }
            binding.progressBar.visibility = if (it.showProgress) View.VISIBLE else View.GONE
            binding.deleteButton.isEnabled = it.enableDeleteButton
        }
        binding.deleteButton.setOnClickListener {
            viewModel.deleteUser()
        }
        return binding.root
    }

    companion object {
        private const val ARG_USER_ID = "ARG_USER_ID"
        fun newInstance(userId: Long): UserDetailsFragment {
            val fragment = UserDetailsFragment()
            fragment.arguments = bundleOf(ARG_USER_ID to userId)
            return fragment
        }
    }
}