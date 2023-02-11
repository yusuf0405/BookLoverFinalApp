package com.example.bookloverfinalapp.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.utils.dialog.LoadingDialog
import com.example.bookloverfinalapp.app.utils.extensions.showToast
import com.example.bookloverfinalapp.app.utils.navigation.NavigationCommand
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences

abstract class BaseBindingFragment<V : ViewBinding>(
    private val binder: (LayoutInflater, ViewGroup?, Boolean) -> V,
) : Fragment() {

    private var viewBinding: V? = null

    protected var binding: V = checkNotNull(viewBinding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = binder.invoke(inflater, container, false)
        viewBinding = binding
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}