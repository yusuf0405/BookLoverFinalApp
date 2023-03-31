package com.example.bookloverfinalapp.app.ui.general_screens.screen_reader.dialog_option

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bookloverfinalapp.R
import com.joseph.utils_core.bindingLifecycleError
import com.example.bookloverfinalapp.databinding.FragmentReaderOptionBinding
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.modal_page.dismissModalPage

class FragmentReaderOption : Fragment(), OnClickListener {

    private var _binding: FragmentReaderOptionBinding? = null
    val binding get() = _binding ?: bindingLifecycleError()

    private var goToPageListener: () -> Unit = {}
    private var goToBookInfoListener: () -> Unit = {}
    private var hideOrShowToolbarListener: () -> Unit = {}
    private var isToolbarState = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReaderOptionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        val hideOrShowToolbarText = if (!isToolbarState)
            R.string.show_toolbar else R.string.hide_toolbar
        hideOrShowToolbar.setText(hideOrShowToolbarText)
    }

    private fun setOnClickListeners() = with(binding) {
        goToPageSetting.setOnClickListener(this@FragmentReaderOption)
        goToBookInfo.setOnClickListener(this@FragmentReaderOption)
        hideToolbar.setOnClickListener(this@FragmentReaderOption)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.go_to_page_setting -> goToPageListener.invoke()
            R.id.hide_toolbar -> {
                dismissModalPage()
                hideOrShowToolbarListener.invoke()
            }
            R.id.go_to_book_info -> {
                dismissModalPage()
                goToBookInfoListener.invoke()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            title: String,
            isToolbarState: Boolean,
            goToPageListener: () -> Unit = {},
            goToBookInfoListener: () -> Unit = {},
            hideOrShowToolbarListener: () -> Unit = {}
        ) = FragmentReaderOption().run {
            this.goToPageListener = goToPageListener
            this.goToBookInfoListener = goToBookInfoListener
            this.hideOrShowToolbarListener = hideOrShowToolbarListener
            this.isToolbarState = isToolbarState
            ModalPage.Builder()
                .fragment(this)
                .title(title)
                .build()
        }
    }
}