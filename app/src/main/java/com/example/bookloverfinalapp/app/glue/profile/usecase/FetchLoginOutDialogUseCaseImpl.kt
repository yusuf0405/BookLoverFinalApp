package com.example.bookloverfinalapp.app.glue.profile.usecase

import androidx.fragment.app.DialogFragment
import com.example.bookloverfinalapp.app.ui.general_screens.screen_profile.login_out.FragmentLoginOutDialog
import com.joseph.profile.domain.usecases.FetchLoginOutDialogUseCase
import dagger.assisted.AssistedFactory
import javax.inject.Inject

class FetchLoginOutDialogUseCaseImpl @Inject constructor(
) : FetchLoginOutDialogUseCase {

    override fun invoke(): DialogFragment = FragmentLoginOutDialog.newInstance()
}