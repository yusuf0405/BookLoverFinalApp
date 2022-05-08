package com.example.bookloverfinalapp.app.ui.screen_sign_up

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookloverfinalapp.databinding.FragmentSignUpBinding
import org.json.JSONObject

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
    }

    private fun setOnClickListeners() {
        binding.apply {
            studentBtn.setOnClickListener(this@FragmentSignUp)
            teacherBtn.setOnClickListener(this@FragmentSignUp)
            signInLink.setOnClickListener(this@FragmentSignUp)
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