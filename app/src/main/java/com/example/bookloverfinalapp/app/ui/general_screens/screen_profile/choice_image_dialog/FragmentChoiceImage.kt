package com.example.bookloverfinalapp.app.ui.general_screens.screen_profile.choice_image_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.SmallHeaderFingerprint
import com.joseph.utils_core.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.FragmentChoiceImageBinding
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.modal_page.dismissModalPage
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.utils_core.extensions.setupLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull


@AndroidEntryPoint
class FragmentChoiceImage : Fragment() {

    private var _binding: FragmentChoiceImageBinding? = null
    val binding get() = _binding ?: bindingLifecycleError()

    private val viewModel: FragmentChoiceImageViewModel by viewModels()

    private var pickInGalleryListener: (() -> Unit)? = null
    private var pickInCameraListener: (() -> Unit)? = null
    private var choiceImageListener: ((Int) -> Unit)? = null

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
            currentItem = adapter::getItemViewType,
            itemId = R.layout.item_default_avatar,
            secondItemId = R.layout.item_user_circle
        )
        pickInGallery.setOnDownEffectClickListener {
            pickInGalleryListener?.invoke()
            dismissModalPage()
        }
        pickInCamera.setOnDownEffectClickListener {
            pickInCameraListener?.invoke()
            dismissModalPage()
        }
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            adapterItems.filter { it.isNotEmpty() }.observe(adapter::submitList)
            choiceImageResourceFlow.filterNotNull().observe(::handleChoiceDefaultImage)
        }
    }

    private fun handleChoiceDefaultImage(resourceId: Int) {
        choiceImageListener?.invoke(resourceId)
        viewModel.clearChoiceImageResourceFlow()
        dismissModalPage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            pickInGalleryListener: () -> Unit,
            choiceImageListener: (Int) -> Unit,
            pickInCameraListener: () -> Unit
        ) = FragmentChoiceImage().run {
            this.pickInGalleryListener = pickInGalleryListener
            this.choiceImageListener = choiceImageListener
            this.pickInCameraListener = pickInCameraListener
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }
}