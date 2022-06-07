package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookloverfinalapp.app.utils.extensions.downEffect
import com.example.bookloverfinalapp.app.utils.navigation.NavigationManager
import com.example.bookloverfinalapp.databinding.FragmentSignUpBinding

class FragmentSignUp : Fragment(), View.OnClickListener {

    private val binding: FragmentSignUpBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentSignUpBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        NavigationManager().loginOut(activity = requireActivity())
    }

    private fun setOnClickListeners() {
        binding.apply {
            downEffect(studentBtn).setOnClickListener(this@FragmentSignUp)
            downEffect(teacherBtn).setOnClickListener(this@FragmentSignUp)
            downEffect(signInLink).setOnClickListener(this@FragmentSignUp)
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.studentBtn -> findNavController().navigate((FragmentSignUpDirections.actionFragmentSignUpToFragmentSignUpStudent()))
            binding.teacherBtn -> findNavController().navigate(FragmentSignUpDirections.actionFragmentSignUpToFragmentSignUpTeacher())
            binding.signInLink -> findNavController().navigate(FragmentSignUpDirections.actionFragmentSignUpToFragmentLogin())
        }
    }
}