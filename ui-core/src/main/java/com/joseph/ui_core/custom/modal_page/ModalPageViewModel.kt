package com.joseph.ui_core.custom.modal_page

import androidx.lifecycle.ViewModel

internal class ModalPageViewModel : ViewModel() {

    var title: String? = null
    var titleVisible: Boolean? = null
    var header: String? = null
    var headerVisible: Boolean? = null
    var closeIconVisible: Boolean? = null
}