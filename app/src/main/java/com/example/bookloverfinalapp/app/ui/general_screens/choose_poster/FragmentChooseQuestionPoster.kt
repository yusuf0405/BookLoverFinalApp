package com.example.bookloverfinalapp.app.ui.general_screens.choose_poster

import android.os.Bundle
import android.view.View
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.adapter.ChooseQuestionPosterPagerAdapter
import com.example.bookloverfinalapp.databinding.FragmentChooseQuestionPosterBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.joseph.ui_core.custom.modal_page.ModalPage

class FragmentChooseQuestionPoster :
    BaseBindingFragment<FragmentChooseQuestionPosterBinding>(FragmentChooseQuestionPosterBinding::inflate) {


    private val defaultConfigureStrategy = TabLayoutMediator.TabConfigurationStrategy { _, _ -> }
    private var tabConfigureStrategy = defaultConfigureStrategy
    private var tabLayoutMediator: TabLayoutMediator? = null

    private var onNetworkPosterSelectedListener: ((String) -> Unit)? = null
    private var onLocalPosterSelectedListener: ((String) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() = with(binding()) {
        pager.adapter = ChooseQuestionPosterPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = viewLifecycleOwner.lifecycle,
            onNetworkPosterSelectedListener = { onNetworkPosterSelectedListener?.invoke(it) },
            onLocalPosterSelectedListener = { onLocalPosterSelectedListener?.invoke(it) }
        )
        tabConfigureStrategy = TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.text = context?.getText(
                when (position) {
                    0 -> R.string.online
                    1 -> R.string.gallery
                    else -> R.string.camera
                }
            )
        }
        tabLayoutMediator = TabLayoutMediator(tabLayout, pager, tabConfigureStrategy)
        tabLayoutMediator?.attach()
    }

    override fun onDestroyView() {
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
        tabConfigureStrategy = defaultConfigureStrategy
        binding().pager.adapter = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(
            onNetworkPosterSelectedListener: (String) -> Unit,
            onLocalPosterSelectedListener: (String) -> Unit,
        ) = FragmentChooseQuestionPoster().run {
            this.onNetworkPosterSelectedListener = onNetworkPosterSelectedListener
            this.onLocalPosterSelectedListener = onLocalPosterSelectedListener
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }
}