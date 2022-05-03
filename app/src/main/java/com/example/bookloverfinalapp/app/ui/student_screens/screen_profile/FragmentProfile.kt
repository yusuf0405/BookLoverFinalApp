package com.example.bookloverfinalapp.app.ui.student_screens.screen_profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.ui.MainActivity
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.app.utils.pref.CurrentUser
import com.example.bookloverfinalapp.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class FragmentProfile :
    BaseFragment<FragmentProfileBinding, FragmentProfileViewModel>(
        FragmentProfileBinding::inflate), View.OnClickListener {
    override val viewModel: FragmentProfileViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInClickListeners()
        setupUi()
    }

    private fun setInClickListeners() {
        binding().apply {
            editProfileBtn.setOnClickListener(this@FragmentProfile)
            profileLogoutImg.setOnClickListener(this@FragmentProfile)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().profileLogoutImg -> loginOut()
            binding().editProfileBtn -> viewModel.goEditProfileFragment()
        }
    }

    private fun loginOut() {
        viewModel.clearDataInPhone()
        viewModel.boosId.observe(viewLifecycleOwner) { pathList ->
            pathList.forEach { path -> File(path).delete() }
            viewModel.clearDataInCache()
            CheсkNavigation().observeLogin(status = false, activity = requireActivity())
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun setupUi() {
        val student = CurrentUser().getCurrentUser(activity = requireActivity())
        binding().apply {
            val fullName = "${student.name} ${student.lastname}"
            profileNameText.text = fullName
            profileSchoolText.text = student.schoolName
            profileClassText.text = student.className

            Glide.with(requireActivity())
                .load(student.image?.url)
                .placeholder(R.drawable.placeholder_avatar)
                .into(binding().profileImg)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }
}