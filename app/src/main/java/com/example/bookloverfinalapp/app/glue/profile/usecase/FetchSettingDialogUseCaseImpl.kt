package com.example.bookloverfinalapp.app.glue.profile.usecase

import android.content.Context
import androidx.fragment.app.DialogFragment
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.FragmentSetting
import com.joseph.profile.domain.usecases.FetchSettingDialogUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FetchSettingDialogUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : FetchSettingDialogUseCase {

    override fun invoke(): DialogFragment =
        FragmentSetting.newInstance(context.getString(R.string.setting))

}