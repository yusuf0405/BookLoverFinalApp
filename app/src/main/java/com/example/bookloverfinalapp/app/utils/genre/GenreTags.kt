package com.example.bookloverfinalapp.app.utils.genre

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.Genre
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.LanguageTypes
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.ItemGenreBinding
import java.util.*


class GenreTags(private val context: Context, private val actionListener: GenreOnClickListener) {

    private val binding: ItemGenreBinding by lazy(LazyThreadSafetyMode.NONE) {
        ItemGenreBinding.bind(LayoutInflater.from(context).inflate(R.layout.item_genre, null))
    }

    fun getGenreTag(genre: Genre): View {
        with(binding) {
            genreTextView.text = checkLanguageAndGetActualString(genre.titles)
            genreTextView.maxLines = 1

            val newLayoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            newLayoutParams.setMargins(8, 8, 8, 8)
            root.layoutParams = newLayoutParams
            root.setOnDownEffectClickListener {
                actionListener.genreItemOnClickListener(
                    genre = genre,
                    textView = genreTextView,
                    container = cardView
                )
            }
        }
        return binding.root
    }

}

private const val ENGLISH = "English"
private const val RUSSIAN = "русский"
private const val UZBEK = "o‘zbek"
private const val KYGYZ = "кыргызча"
fun checkLanguageAndGetActualString(strings: List<String>): String {
    return when (Locale.getDefault().displayLanguage) {
        ENGLISH -> {
            if (strings.isEmpty()) String()
            else strings.first()
        }
        RUSSIAN -> {
            if (strings.size < 2) String()
            else strings[1]
        }
        UZBEK -> {
            if (strings.size < 3) String()
            else strings[2]
        }
        KYGYZ -> {
            if (strings.size < 4) String()
            else strings[3]
        }
        else -> {
            if (strings.isEmpty()) String()
            else strings.first()
        }
    }
}

interface GenreOnClickListener {

    fun genreItemOnClickListener(genre: Genre, textView: TextView, container: CardView) = Unit

}