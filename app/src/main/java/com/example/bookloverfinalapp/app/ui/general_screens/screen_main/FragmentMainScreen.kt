package com.example.bookloverfinalapp.app.ui.general_screens.screen_main

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.CustomPopupMenu
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.WorkManager
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book.UploadFileType
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialogClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.confim_dialog.FragmentConfirmDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details.HorizontalUserFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.FragmentSetting
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.HorizontalItemsFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.HorizontalItemsFingerprintSecond
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenAudioBookBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenBookBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenCollectionsBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenExclusiveAudioBookBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenExclusiveBookBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenPopularGenreBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenStoriesBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenUserBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.AddNewBooksFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.AddStoriesFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.AudioBookHorizontalFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.AudioBookSmallHorizontalFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.BookGenreFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.BookHorizontalFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.BookHorizontalSmallFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.ButtonFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.CollectionsFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.ExclusiveAudioBookFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.ExclusiveBookFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.HeaderFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.MainScreenErrorFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.MainScreenShimmerFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.SavedBookMainFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.SelectFavoriteBooksFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.StoriesFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.TaskFingerprint
import com.example.bookloverfinalapp.app.ui.service_player.PlayerCallback
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.workers.CheckBookIsSaveWorker
import com.example.bookloverfinalapp.databinding.FragmentMainScreenBinding
import com.joseph.core.extensions.setupLayoutManager
import com.joseph.core.motion.MotionListener
import com.joseph.core.motion.MotionState
import com.joseph.stories.presentation.dialog.choice_file_type.FragmentChoiceUploadFileForStoriesDialog
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.extensions.launchWhenViewStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import com.joseph.ui.core.R as UiCore


@AndroidEntryPoint
class FragmentMainScreen :
    BaseFragment<FragmentMainScreenBinding, FragmentMainScreenViewModel>(FragmentMainScreenBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener, FragmentBookOptionDialogClickListeners {

    override val viewModel: FragmentMainScreenViewModel by viewModels()

    private val genericAdapter = FingerprintAdapter(
        listOf(
            MainScreenShimmerFingerprint(),
            MainScreenErrorFingerprint(),
            MainScreenCollectionsBlockFingerprint(listOf(CollectionsFingerprint())),
            HeaderFingerprint(),
            MainScreenStoriesBlockFingerprint(
                listOf(
                    AddStoriesFingerprint(),
                    StoriesFingerprint()
                )
            ),
            SelectFavoriteBooksFingerprint(),
            AddNewBooksFingerprint(),
            HorizontalItemsFingerprintSecond(listOf(TaskFingerprint())),
            HeaderFingerprint(),
            MainScreenAudioBookBlockFingerprint(listOf(AudioBookHorizontalFingerprint())),
            MainScreenPopularGenreBlockFingerprint(
                listOf(BookHorizontalSmallFingerprint(), AudioBookSmallHorizontalFingerprint()),
            ),
            HeaderFingerprint(),
            MainScreenExclusiveAudioBookBlockFingerprint(listOf(ExclusiveAudioBookFingerprint())),
            HeaderFingerprint(),
            MainScreenExclusiveBookBlockFingerprint(listOf(ExclusiveBookFingerprint())),
            HeaderFingerprint(),
            HorizontalItemsFingerprint(listOf(SavedBookMainFingerprint())),
            HeaderFingerprint(),
            MainScreenBookBlockFingerprint(listOf(BookHorizontalFingerprint())),
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
                listOf(HorizontalUserFingerprint(isWrapContent = true)),
                RecyclerView.RecycledViewPool(),
            ),
        )
    )
    var concatAdapter: ConcatAdapter = ConcatAdapter(
        ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build(),
        genericAdapter,
        genresAdapter,
        usersAdapter,
    )

    private val motionListener = MotionListener(::setToolbarState)

    private var playerCallback: PlayerCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playerCallback = context as? PlayerCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isHaveToolbar = true
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigationView()
        setupViews()
        setOnClickListeners()
        observeData()
        checkPostNotificationsPermission()
        startCheckBookIsSaveWorker()
    }

    private fun startCheckBookIsSaveWorker() {
        val workManager = WorkManager.getInstance(requireContext())
        val worker = CheckBookIsSaveWorker.getCheckBookIsSaveWorker()
        workManager.enqueue(worker)
    }

    private fun setupViews() = with(binding()) {
//        root.addTransitionListener(motionListener)
        bookRecyclerView.adapter = concatAdapter

        bookRecyclerView.setupLayoutManager(
            currentItem = concatAdapter::getItemViewType,
            itemId = R.layout.item_book_genre,
            secondItemId = R.layout.item_user_circle
        )
    }

    private fun checkPostNotificationsPermission() = if (ContextCompat.checkSelfPermission(
            requireContext(),
            POST_NOTIFICATIONS
        ) != PermissionChecker.PERMISSION_GRANTED
    ) {
        resultPickReadExternalStorage.launch(POST_NOTIFICATIONS)
        false
    } else true

    private val resultPickReadExternalStorage =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) return@registerForActivityResult
        }

    private fun observeData() = with(viewModel) {
        launchWhenViewStarted {
            allFilteredItemsFlow.filterNotNull().observe(::populateModels)
            currentUserFlow.observe(::setupAvatarAndNameBlock)
            showConfirmDialogFlow.observe(::showConfirmDialogFlow)
            playAudioBookFlow.observe { playerCallback?.play(it) }
            showBookOptionDialogFlow.observe(::showFragmentBookOptionDialog)
            showAddStoriesDialogFlow.observe { showFragmentChoiceUploadFileForStoriesDialog() }
            fefef.observe {
                hideBottomNavigationView()
            }
        }
    }

    private fun setToolbarState(state: MotionState) {
        when (state) {
            MotionState.COLLAPSED -> viewModel.updateMotionPosition(COLLAPSED)
            MotionState.EXPANDED -> viewModel.updateMotionPosition(EXPANDED)
            else -> Unit
        }
    }

    private fun setupAvatarAndNameBlock(user: User) = with(binding().includeMainScreenToolbar) {
//        val greetingTextList = arrayListOf(
//            getString(R.string.find_you_best_book),
//            getString(R.string.we_wish_you_good_time),
//            getString(R.string.pump_your_brains),
//        )
//        val greetingText = "${getString(R.string.hello_plus)} ${user.name}!"
//        userFullName.text = greetingText
//        randomText.text = greetingTextList.random()
//        requireContext().showImage(user.image?.url, avatar)
    }

    private fun setOnClickListeners() = with(binding()) {
//        avatarAndNameBlock.avatar.setOnDownEffectClickListener { viewModel.navigateToProfileFragment() }
        includeMainScreenToolbar.addIcon.setOnDownEffectClickListener(::showAddPopupMenu)
        includeMainScreenToolbar.searchIcon.setOnDownEffectClickListener {
            hideBottomNavigationView()
            viewModel.navigateToSearchFragment()
        }
        includeMainScreenToolbar.settingIcon.setOnDownEffectClickListener { showSettingModalPage() }
    }

    private fun showAddPopupMenu(view: View) {
        val popupMenu =
            CustomPopupMenu(
                context = requireContext(),
                view = view,
                gravity = Gravity.END,
                com.joseph.ui.core.R.style.PopupMenuDefaultStyle
            )

        popupMenu.menu.add(0, ID_ADD_BOOK, Menu.NONE, getString(UiCore.string.audio_book))
            .apply {
                setIcon(UiCore.drawable.audio_icon)
            }
        popupMenu.menu.add(0, ID_ADD_AUDIO_BOOK, Menu.NONE, getString(UiCore.string.book))
            .apply {
                setIcon(UiCore.drawable.read_book_icon)
            }
        popupMenu.menu.add(0, ID_ADD_STORIES, Menu.NONE, getString(UiCore.string.stories))
            .apply {
                setIcon(UiCore.drawable.star_icon)
            }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_ADD_BOOK -> navigateUploadFragment(UploadFileType.PDF_BOOK)
                ID_ADD_AUDIO_BOOK -> navigateUploadFragment(UploadFileType.AUDIO_BOOK)
                ID_ADD_STORIES -> showFragmentChoiceUploadFileForStoriesDialog()
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    private fun navigateUploadFragment(type: UploadFileType) =
        viewModel.navigateUploadBookFragment(type)

    private fun populateModels(items: Triple<List<Item>, List<Item>, List<Item>>) {
        saveRecyclerViewCurrentState()
        genericAdapter.submitList(items.first) { restoreRecyclerViewCurrentState() }
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

    private fun showFragmentBookOptionDialog(bookId: String) = FragmentBookOptionDialog
        .newInstance(bookId = bookId, listener = this)
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun showFragmentChoiceUploadFileForStoriesDialog() =
        FragmentChoiceUploadFileForStoriesDialog.newInstance()
            .show(requireActivity().supportFragmentManager, ModalPage.TAG)


    private fun showConfirmDialogFlow(id: String) = FragmentConfirmDialog.newInstance(
        setOnPositiveButtonClickListener = { viewModel.deleteBookInSavedBooks(id) }
    ).show(requireActivity().supportFragmentManager, ModalPage.TAG)


    private fun showSettingModalPage() = FragmentSetting.newInstance(getString(UiCore.string.setting))
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
//        binding().root.progress = viewModel.motionPosition.value
    }

    override fun onDestroyView() {
//        binding().root.removeTransitionListener(motionListener)
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        playerCallback = null
    }

    private companion object {
        private const val ID_ADD_BOOK = 1
        private const val ID_ADD_AUDIO_BOOK = 2
        private const val ID_ADD_STORIES = 3
    }
}