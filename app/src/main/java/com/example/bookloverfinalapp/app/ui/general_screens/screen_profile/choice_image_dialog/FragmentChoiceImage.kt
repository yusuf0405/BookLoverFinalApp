package com.example.bookloverfinalapp.app.ui.general_screens.screen_profile.choice_image_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.SmallHeaderFingerprint
import com.example.bookloverfinalapp.app.utils.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.setupLayoutManager
import com.example.bookloverfinalapp.databinding.FragmentChoiceImageBinding
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.modal_page.dismissModalPage
import com.joseph.ui_core.extensions.launchWhenViewStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter


@AndroidEntryPoint
class FragmentChoiceImage : Fragment() {

    private var _binding: FragmentChoiceImageBinding? = null
    val binding get() = _binding ?: bindingLifecycleError()

    private val viewModel: FragmentChoiceImageViewModel by viewModels()

    private var pickInGalleryListener: (() -> Unit)? = null

    private var adapter = FingerprintAdapter(
        listOf(
            SmallHeaderFingerprint(),
            DefaultAvatarFingerprint(),
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoiceImageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeResource()
    }

    private fun setupViews() = with(binding) {
        recyclerView.adapter = adapter
        recyclerView.setupLayoutManager(
            adapter = adapter,
            R.layout.item_default_avatar
        )
        pickInGallery.setOnDownEffectClickListener {
            pickInGalleryListener?.invoke()
            dismissModalPage()
        }
        pickInCamera.setOnDownEffectClickListener {
            pickInGalleryListener?.invoke()
            dismissModalPage()
        }
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            adapterItems
                .filter { it.isNotEmpty() }
                .observe(adapter::submitList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            pickInGalleryListener: () -> Unit
        ) = FragmentChoiceImage().run {
            this.pickInGalleryListener = pickInGalleryListener
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }
}