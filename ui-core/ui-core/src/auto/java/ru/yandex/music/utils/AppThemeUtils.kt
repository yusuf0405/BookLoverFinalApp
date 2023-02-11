package ru.yandex.music.utils

import android.content.Context
import androidx.annotation.StyleRes
import ru.mts.music.uicore.R
import ru.yandex.music.ui.AppTheme

class AppThemeUtils {

    companion object {
        @StyleRes
        fun getActivityTheme(appTheme: AppTheme): Int {
            return R.style.AppThemeAuto_Dark
            //Вернуть эту строчку когда появится смена тем для авто магнитол в приложении МТС
//            return if (appTheme == AppTheme.LIGHT) R.style.AppThemeAuto else R.style.AppThemeAuto_Dark
        }

        fun getHeaderViewHeight(context: Context): Int {
            return 250 // такая высота плейлистов выглядит хорошо на магнитоле
        }

        // Дефолтное количество колонок
        const val defaultSpanCount = 3

        const val showLikeRateView = false

        const val showUpdateBanner = false

        const val showMixScreenBanner = false

        const val columnGenresForSearchScreen = 4

        const val columnPodcastForSearchScreen = 1

        const val isScrollViewPager = false

    }
}