package com.example.bookloverfinalapp.app.ui.service_player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.SettingSelectionAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.SettingSelectionItem
import com.joseph.core.bindingLifecycleError
import com.example.bookloverfinalapp.databinding.FragmentPlaybackSpeedDialogBinding
import java.text.DecimalFormat
import com.example.bookloverfinalapp.R as MainRes

class FragmentPlaybackSpeedDialog : Fragment(),
    SettingSelectionAdapter.OnItemSelectionListener {

    private var _binding: FragmentPlaybackSpeedDialogBinding? = null
    val binding get() = _binding ?: bindingLifecycleError()

    interface OnPlaybackChangedListener {
        fun onPlaybackSpeedChanged(playbackSpeed: Float)
    }

    private val currentSpeed: String by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getString(CURRENT_SPEED_KEY) ?: String()
    }

    private val adapter: SettingSelectionAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SettingSelectionAdapter(onItemSelectionListener = this)
    }

    private val listener: OnPlaybackChangedListener by lazy(LazyThreadSafetyMode.NONE) {
        when {
            targetFragment is OnPlaybackChangedListener -> targetFragment as OnPlaybackChangedListener
            activity is OnPlaybackChangedListener -> activity as OnPlaybackChangedListener
            else -> throw IllegalArgumentException("Activity: $activity, or target fragment: $targetFragment doesn't implement ${OnPlaybackChangedListener::class.java.name}")
        }
    }

    private val speedNumberItems = mutableMapOf<String, String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaybackSpeedDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() = with(binding) {
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = null
        val items = requireContext()
            .resources
            .getStringArray(MainRes.array.playback_speed_options).map {

                val formatter = DecimalFormat("0.#")
                val title = getString(R.string.playback_speed, formatter.format(it.toFloat()))
                speedNumberItems[title] = it
                SettingSelectionItem(title = title, isChecked = currentSpeed == title)
            }
        adapter.populate(items)
    }

    override fun onItemSelected(item: SettingSelectionItem) {
        val speed = speedNumberItems[item.title] ?: return
        listener.onPlaybackSpeedChanged(speed.toFloat())
    }

    companion object {
        private const val CURRENT_SPEED_KEY = "CURRENT_SPEED_KEY"
        fun newInstance(currentSpeed: String): FragmentPlaybackSpeedDialog {
            val args = Bundle()
            args.putString(CURRENT_SPEED_KEY, currentSpeed)
            val fragment = FragmentPlaybackSpeedDialog()
            fragment.arguments = args
            return fragment
        }
    }
}