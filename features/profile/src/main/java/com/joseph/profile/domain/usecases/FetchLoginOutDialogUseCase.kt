package com.joseph.profile.domain.usecases

import androidx.fragment.app.DialogFragment

interface FetchLoginOutDialogUseCase {

    operator fun invoke(): DialogFragment
}