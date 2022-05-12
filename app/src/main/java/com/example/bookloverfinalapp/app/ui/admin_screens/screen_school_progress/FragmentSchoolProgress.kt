package com.example.bookloverfinalapp.app.ui.admin_screens.screen_school_progress

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookloverfinalapp.R

class FragmentSchoolProgress : Fragment() {

    companion object {
        fun newInstance() = FragmentSchoolProgress()
    }

    private lateinit var viewModel: FragmentSchoolProgressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_school_progress, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FragmentSchoolProgressViewModel::class.java)
        // TODO: Use the ViewModel
    }

}