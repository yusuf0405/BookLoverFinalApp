package com.example.bookloverfinalapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.bookloverfinalapp.app.utils.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.FragmentGoToPageBinding
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.modal_page.dismissModalPage

class FragmentGoToPage : Fragment() {

    private var _binding: FragmentGoToPageBinding? = null
    val binding get() = _binding ?: bindingLifecycleError()


    private var listener: ((Int) -> Unit?)? = null
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoToPageBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        setupViews()
    }

    private fun setupViews() = with(binding) {
        goPageEditText.setText(currentPage.toString())
        goPageEditText.requestFocus()
        confirmButton.setOnDownEffectClickListener { handleConfirmButtonClick() }
        cancelButton.setOnDownEffectClickListener { dismissModalPage() }
    }

    private fun handleConfirmButtonClick() = with(binding) {
        val enteredText = goPageEditText.text.toString().trim()
        if (enteredText.isBlank()) {
            goPageEditText.error = getString(R.string.empty_value)
            return@with
        }
        val newPage = enteredText.toInt()
        listener?.invoke(newPage)
        dismissModalPage()
    }

    companion object {
        fun newInstance(title: String, currentPage: Int, listener: (Int) -> Unit) =
            FragmentGoToPage().run {
                this.listener = listener
                this.currentPage = currentPage
                ModalPage.Builder()
                    .fragment(this)
                    .title(title)
                    .build()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}