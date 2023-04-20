package com.example.recycleview.domain

import androidx.fragment.app.Fragment
import com.example.recycleview.presentation.App
import com.example.recycleview.presentation.ViewModelFactory

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator
