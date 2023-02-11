package com.example.bookloverfinalapp.app.ui.general_screens.screen_main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SearchView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.ui.MotionListener
import com.example.bookloverfinalapp.app.ui.MotionState
import com.example.bookloverfinalapp.app.ui.adapter.GroupVerticalItemDecoration
import com.example.bookloverfinalapp.app.ui.adapter.animations.AddableItemAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SimpleCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInLeftCommonAnimator
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book.UploadFileType
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialogClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details.HorizontalUserFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.FragmentSetting
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.GenreBlockAdapterItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.UserBlockAdapterItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.*
import com.example.bookloverfinalapp.app.ui.player.PlayerCallback
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.FragmentMainScreenBinding
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.ui_core.extensions.toDp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull


@AndroidEntryPoint
class FragmentMainScreen :
    BaseFragment<FragmentMainScreenBinding, FragmentMainScreenViewModel>(FragmentMainScreenBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener, FragmentBookOptionDialogClickListeners {

    override val viewModel: FragmentMainScreenViewModel by viewModels()

    private val bookAdapter = FingerprintAdapter(
        listOf(
            MainScreenShimmerFingerprint(),
            MainScreenErrorFingerprint(),
            HeaderFingerprint(),
            HorizontalItemsFingerprintSecond(
                listOf(TaskFingerprint()),
                RecyclerView.RecycledViewPool()
            ),
            HeaderFingerprint(),
            MainScreenAudioBookBlockFingerprint(
                listOf(AudioBookHorizontalFingerprint()),
                RecyclerView.RecycledViewPool()

            ),
            MainScreenPopularGenreBlockFingerprint(
                listOf(BookHorizontalSmallFingerprint(), AudioBookSmallHorizontalFingerprint()),
                RecyclerView.RecycledViewPool()
            ),
            HeaderFingerprint(),
            MainScreenExclusiveBookBlockFingerprint(
                listOf(ExclusiveBookFingerprint()),
                RecyclerView.RecycledViewPool()
            ),

            HeaderFingerprint(),
            HorizontalItemsFingerprint(
                listOf(SavedBookMainFingerprint()),
                RecyclerView.RecycledViewPool()
            ),
            HeaderFingerprint(),
            MainScreenBookBlockFingerprint(
                listOf(BookHorizontalFingerprint()),
                RecyclerView.RecycledViewPool()
            ),
        )
    )

    private val genresAdapter = FingerprintAdapter(
        listOf(
            HeaderFingerprint(),
            BookGenreFingerprint(),
            ButtonFingerprint()
        )
    )

    private val usersAdapter = FingerprintAdapter(
        listOf(
            HeaderFingerprint(),
            MainScreenUserBlockFingerprint(
                listOf(HorizontalUserFingerprint()),
                RecyclerView.RecycledViewPool(),
            ),
        )
    )
    var concatAdapter: ConcatAdapter = ConcatAdapter(
        ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build(),
        bookAdapter,
        genresAdapter,
        usersAdapter,
    )


    private val motionListener = MotionListener(::setToolbarState)

    private val rotateOpenAnim: Animation by lazy(LazyThreadSafetyMode.NONE) {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim)
    }
    private val rotateCloseAnim: Animation by lazy(LazyThreadSafetyMode.NONE) {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim)
    }
    private val fromBottomAnim: Animation by lazy(LazyThreadSafetyMode.NONE) {
        AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim)
    }
    private val toBottomAnim: Animation by lazy(LazyThreadSafetyMode.NONE) {
        AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim)
    }

    private var playerCallback: PlayerCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playerCallback = context as? PlayerCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigationView()
        setupViews()
        setOnClickListeners()
        observeData()
    }

    private fun setupViews() = with(binding()) {
        root.addTransitionListener(motionListener)
        bookRecyclerView.adapter = concatAdapter
        setupLayoutManager()
    }

    private fun setupLayoutManager() = with(binding().bookRecyclerView) {
        if (layoutManager !is GridLayoutManager) return
        val gridLayoutManager = layoutManager as? GridLayoutManager
        gridLayoutManager?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(viewType: Int) =
                when (concatAdapter.getItemViewType(viewType)) {
                    R.layout.item_book_genre -> 1
                    else -> 2
                }
        }
    }

    private fun observeData() = with(viewModel) {
        launchWhenViewStarted {
            allFilteredItemsFlow.filterNotNull().observe(::populateModels)
            currentUserFlow.observe(::setupAvatarAndNameBlock)
            showConfirmDialogFlow.observe(::showConfirmDialogFlow)
            playAudioBookFlow.observe { playerCallback?.play(it) }
            showBookOptionDialogFlow.observe(::showFragmentBookOptionDialog)
        }
    }

    private fun setToolbarState(state: MotionState) {
        when (state) {
            MotionState.COLLAPSED -> viewModel.updateMotionPosition(COLLAPSED)
            MotionState.EXPANDED -> viewModel.updateMotionPosition(EXPANDED)
            else -> Unit
        }
    }

    private fun setupAvatarAndNameBlock(user: User) = with(binding().avatarAndNameBlock) {
        val greetingTextList = arrayListOf(
            getString(R.string.find_you_best_book),
            getString(R.string.we_wish_you_good_time),
            getString(R.string.pump_your_brains),
        )
        val greetingText = "${getString(R.string.hello_plus)} ${user.fullName()}!"
        userFullName.text = greetingText
        randomText.text = greetingTextList.random()
        requireContext().showImage(user.image?.url, avatar)
    }

    private fun setOnClickListeners() = with(binding()) {
        avatarAndNameBlock.avatar.setOnDownEffectClickListener { viewModel.navigateToProfileFragment() }
        avatarAndNameBlock.searchIcon.setOnDownEffectClickListener { viewModel.navigateToSearchFragment() }
        avatarAndNameBlock.settingIcon.setOnDownEffectClickListener { showSettingModalPage() }
        addPdfBook.setOnDownEffectClickListener {
            viewModel.navigateUploadBookFragment(UploadFileType.PDF_BOOK)
        }
        addAudioBook.setOnDownEffectClickListener {
            viewModel.navigateUploadBookFragment(UploadFileType.AUDIO_BOOK)
        }
        floatingActionButton.setOnDownEffectClickListener { onAddButtonClicked() }
    }

    private fun populateModels(items: Triple<List<Item>, List<Item>, List<Item>>) {
        saveRecyclerViewCurrentState()
        bookAdapter.submitList(items.first) { restoreRecyclerViewCurrentState() }
        genresAdapter.submitList(items.second)
        usersAdapter.submitList(items.third)
    }

    private fun saveRecyclerViewCurrentState() {
        val currentState = binding().bookRecyclerView.layoutManager?.onSaveInstanceState()
        viewModel.saveRecyclerViewCurrentState(currentState)
    }

    private fun restoreRecyclerViewCurrentState() {
        val currentPosition = viewModel.fetchRecyclerViewCurrentState()
        binding().bookRecyclerView.layoutManager?.onRestoreInstanceState(currentPosition)
    }

    private fun onAddButtonClicked() {
        setVisibility()
        setAnimation()
        viewModel.updateFloatingActionButtonIsClickedFlow(!viewModel.floatingActionButtonIsClickedFlow())
    }

    private fun setVisibility() = with(binding()) {
        addPdfBook.isVisible = viewModel.floatingActionButtonIsClickedFlow()
        addAudioBook.isInvisible = viewModel.floatingActionButtonIsClickedFlow()
    }

    private fun setAnimation() = with(binding()) {
        if (!viewModel.floatingActionButtonIsClickedFlow()) {
            addAudioBook.startAnimation(fromBottomAnim)
            addPdfBook.startAnimation(fromBottomAnim)
            floatingActionButton.startAnimation(rotateOpenAnim)
            return@with
        }
        addAudioBook.startAnimation(toBottomAnim)
        addPdfBook.startAnimation(toBottomAnim)
        floatingActionButton.startAnimation(rotateCloseAnim)
    }

    private fun showFragmentBookOptionDialog(bookId: String) = FragmentBookOptionDialog
        .newInstance(bookId = bookId, listener = this)
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun showConfirmDialogFlow(id: String) {
        createConfirmationDialog(
            title = R.string.default_delete_alert_message,
            positiveButtonMessage = R.string.action_yes,
            negativeButtonMessage = R.string.action_no,
            iconId = R.drawable.ic_delete,
            setOnPositiveButtonClickListener = { viewModel.deleteBookInSavedBooks(id) }
        ).show()
    }

    private fun showSettingModalPage() = FragmentSetting.newInstance(getString(R.string.setting))
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    override fun addQuestionOnClickListener(bookId: String) {
        viewModel.navigateToCreateQuestionFragment(bookId = bookId)
    }

    override fun editBookOnClickListener(bookId: String) {
        viewModel.navigateToEditBookFragment(bookId = bookId)
    }

    override fun bookReadOnClickListener(savedBook: BookThatRead) {
        viewModel.navigateToBookChaptersFragment(savedBook)
    }

    override fun onRefresh() {
//        binding().swipeRefresh.apply {
//            isRefreshing = true
//            postDelayed({ isRefreshing = false }, 1500)
//        }
    }

    override fun onStart() {
        super.onStart()
        binding().root.progress = viewModel.motionPosition.value
    }

    override fun onDestroyView() {
        binding().root.removeTransitionListener(motionListener)
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        playerCallback = null
    }
}