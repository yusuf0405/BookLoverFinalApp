package com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.choose_time

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.adapter.ChooseModelFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.FingerprintAdapter
import com.example.bookloverfinalapp.app.utils.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.tuneBottomDialog
import com.example.bookloverfinalapp.app.utils.extensions.tuneLyricsDialog
import com.example.bookloverfinalapp.databinding.FragmentChoosePointBinding
import com.joseph.ui_core.extensions.launchOnLifecycle
import com.joseph.ui_core.extensions.launchWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FragmentChooseTimeDialog : DialogFragment() {

    private var _binding: FragmentChoosePointBinding? = null
    private val binding get() = _binding ?: bindingLifecycleError()

    private var adapter = FingerprintAdapter(
        listOf(ChooseModelFingerprint())
    )

    private val viewModel: FragmentChooseTimeViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        tuneBottomDialog()
        tuneLyricsDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoosePointBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setWindowAnimations(com.joseph.ui_core.R.style.ModalPage_Animation)
        setupViews()
        observeResource()
    }

    private fun setupViews() = with(binding) {
        recyclerView.adapter = adapter
    }

    private fun observeResource() = with(viewModel) {
        launchOnLifecycle {
            itemsFlow.observe {
                Log.i("Joseph", it.toString())
                adapter.submitList(it)
                update(it)
            }
        }
    }

}