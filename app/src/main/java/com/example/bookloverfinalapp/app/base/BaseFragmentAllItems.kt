package com.example.bookloverfinalapp.app.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.joseph.core.motion.MotionListener
import com.joseph.core.motion.MotionState
import com.example.bookloverfinalapp.app.ui.adapter.animations.AddableItemAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SimpleCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInLeftCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInTopCommonAnimator
import com.example.bookloverfinalapp.app.ui.general_screens.activity_main.OnBackPressedListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialogClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.confim_dialog.FragmentConfirmDialog
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.joseph.ui.core.adapter.Item
import com.example.bookloverfinalapp.app.ui.service_player.PlayerCallback
import com.joseph.core.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.FragmentAllItemsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.custom.snackbar.GenericSnackbar
import com.joseph.ui.core.extensions.launchWhenViewStarted
import com.joseph.core.extensions.setupLayoutManager
import com.joseph.core.extensions.showOnlyOne
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
abstract class BaseFragmentAllItems : Fragment(), OnBackPressedListener,
    FragmentBookOptionDialogClickListeners {

    protected val viewModel: BaseItemsViewModel by viewModels()

    protected abstract val allItemsFetchType: AllItemsFetchType

    protected abstract val adapter: FingerprintAdapter

    protected abstract val toolbarTitle: Int

    protected abstract val sortDialogFragment: DialogFragment

    protected abstract val layoutManager: LinearLayoutManager

    private val motionListener = MotionListener(::setToolbarState)

    private var _binding: FragmentAllItemsBinding? = null
    private val binding get() = _binding ?: bindingLifecycleError()

    private val boomNav: BottomNavigationView? by lazy(LazyThreadSafetyMode.NONE) {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView) ?: null
    }

    private var playerCallback: PlayerCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playerCallback = context as? PlayerCallback
    }

    override fun onStart() {
        super.onStart()
        binding.root.progress = viewModel.motionPosition.value
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAllItemsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        setupViews()
        setOnClickListeners()
        observeRecourse()
    }

    private fun setupViews() = with(binding) {
        root.setPaddingTopHeightStatusBar()
        root.addTransitionListener(motionListener)
        title.setText(toolbarTitle)
        setupRecyclerView()
    }

    private fun setupRecyclerView() = with(binding.itemsRecyclerView) {
        adapter = this@BaseFragmentAllItems.adapter
        layoutManager = layoutManager
        setupLayoutManager(
            currentItem = this@BaseFragmentAllItems.adapter::getItemViewType,
            itemId = R.layout.item_book_genre,
            secondItemId = R.layout.item_user_circle
        )
        itemAnimator = createAddableItemAnimator()
    }

    private fun setOnClickListeners() = with(binding) {
        upButton.setOnDownEffectClickListener { findNavController().navigateUp() }
        sortOptions.setOnDownEffectClickListener { showSortDialogFragment() }
    }

    private fun observeRecourse() = with(viewModel) {
        launchWhenViewStarted {
            updateAllItemsTypeFlow(allItemsFetchType)
            navCommand.observe(findNavController()::navigateTo)
            showBookOptionDialogFlow.observe(::showFragmentBookOptionDialog)
            isErrorMessageIdFlow.observe { showErrorSnackbar(it.format(requireContext())) }
            isErrorMessageFlow.observe(::showErrorSnackbar)
            showConfirmDialogFlow.observe(::showConfirmDialogFlow)
            playAudioBookFlow.observe { audioBookId -> playerCallback?.play(audioBookId) }
            showSuccessSnackbarFlow.observe { showSuccessSnackBar(it.format(requireContext())) }
            allItemsFlow.filter { it.isNotEmpty() }.observe(::populateModels)
        }
    }

    private fun populateModels(items: List<Item>) {
        saveRecyclerViewCurrentState()
        adapter.submitList(items) { restoreRecyclerViewCurrentState() }
    }

    private fun saveRecyclerViewCurrentState() {
        val currentState = binding.itemsRecyclerView.layoutManager?.onSaveInstanceState()
        viewModel.saveRecyclerViewCurrentState(currentState)
    }

    private fun restoreRecyclerViewCurrentState() {
        val currentPosition = viewModel.fetchRecyclerViewCurrentState()
        binding.itemsRecyclerView.layoutManager?.onRestoreInstanceState(currentPosition)
    }

    private fun setToolbarState(state: MotionState) {
        when (state) {
            MotionState.COLLAPSED -> viewModel.updateMotionPosition(COLLAPSED)
            MotionState.EXPANDED -> viewModel.updateMotionPosition(EXPANDED)
            else -> Unit
        }
    }

    private fun createAddableItemAnimator() =
        AddableItemAnimator(SimpleCommonAnimator()).also { anim ->
            anim.addViewTypeAnimation(
                R.layout.item_book,
                SlideInLeftCommonAnimator()
            )
            anim.addViewTypeAnimation(
                R.layout.item_saved_book,
                SlideInLeftCommonAnimator()
            )
            anim.addViewTypeAnimation(
                R.layout.item_search_view,
                SlideInTopCommonAnimator()
            )
            anim.addViewTypeAnimation(
                R.layout.item_user,
                SlideInLeftCommonAnimator()
            )
            anim.addViewTypeAnimation(
                R.layout.item_choice_genre,
                SlideInLeftCommonAnimator()
            )
            anim.addViewTypeAnimation(
                R.layout.item_audio_book,
                SlideInLeftCommonAnimator()
            )
            anim.addViewTypeAnimation(
                R.layout.item_task,
                SlideInLeftCommonAnimator()
            )
            anim.addDuration = DEFAULT_ITEMS_ANIMATOR_DURATION
            anim.removeDuration = DEFAULT_ITEMS_ANIMATOR_DURATION
        }

    private fun showFragmentBookOptionDialog(bookId: String) = FragmentBookOptionDialog
        .newInstance(bookId = bookId, listener = this)
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun showSortDialogFragment() =
        sortDialogFragment.showOnlyOne(requireActivity().supportFragmentManager)

    private fun showConfirmDialogFlow(id: String) {
        FragmentConfirmDialog.newInstance(
            setOnPositiveButtonClickListener = { viewModel.deleteBookInSavedBooks(id) }
        ).show(requireActivity().supportFragmentManager, ModalPage.TAG)
    }

    private fun hideBottomNavigationView() = boomNav?.apply { hide() }

    private fun showErrorSnackbar(message: String) = GenericSnackbar
        .Builder(binding.root)
        .error()
        .message(message)
        .build()
        .show()

    private fun showSuccessSnackBar(message: String) = GenericSnackbar
        .Builder(binding.root)
        .success()
        .message(message)
        .build()
        .show()

    override fun bookReadOnClickListener(savedBook: BookThatRead) {
        viewModel.navigateToBookReadFragment(savedBook = savedBook)
    }

    override fun addQuestionOnClickListener(bookId: String) {
        viewModel.navigateToCreateQuestionFragment(bookId)
    }

    override fun editBookOnClickListener(bookId: String) {
        viewModel.navigateToEditBookFragment(bookId)
    }

    private var isBack = AtomicBoolean(true)
    override fun onBackPressed(): Boolean {
        if (!isBack.get()) return false
        adapter.submitList(emptyList()) {
            isBack.set(false)
            lifecycleScope.launch {
                delay(DEFAULT_ITEMS_ANIMATOR_DURATION)
                requireActivity().onBackPressed()
            }
        }
        return isBack.get()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.root.removeTransitionListener(motionListener)
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        playerCallback = null
    }

    companion object {
        const val COLLAPSED = 1f
        const val EXPANDED = 0f
        const val DEFAULT_ITEMS_ANIMATOR_DURATION = 500L
    }
}

enum class AllItemsFetchType {
    ALL_BOOKS,
    ALL_SAVED_BOOKS,
    ALL_AUDIO_BOOKS,
    ALL_GENRES,
    ALL_USERS,
    ALL_TASKS,
    NONE
}