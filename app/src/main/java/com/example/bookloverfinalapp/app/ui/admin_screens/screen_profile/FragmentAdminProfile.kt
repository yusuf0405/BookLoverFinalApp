package com.example.bookloverfinalapp.app.ui.admin_screens.screen_profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookloverfinalapp.R

class FragmentAdminProfile : Fragment() {

    companion object {
        fun newInstance() = FragmentAdminProfile()
    }

    private lateinit var viewModel: FragmentAdminProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FragmentAdminProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}