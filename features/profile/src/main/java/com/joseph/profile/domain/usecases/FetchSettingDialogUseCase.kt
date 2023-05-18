package com.joseph.profile.domain.usecases

import androidx.fragment.app.DialogFragment

interface FetchSettingDialogUseCase {

    operator fun invoke(): DialogFragment
}