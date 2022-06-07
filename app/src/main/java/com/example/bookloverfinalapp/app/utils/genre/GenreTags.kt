package com.example.bookloverfinalapp.app.utils.genre

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.extensions.downEffect
import com.example.bookloverfinalapp.databinding.ItemGenreBinding


class GenreTags(private val context: Context, private val actionListener: GenreOnClickListener) {

    private val binding: ItemGenreBinding by lazy(LazyThreadSafetyMode.NONE) {
        ItemGenreBinding.bind(LayoutInflater.from(context).inflate(R.layout.item_genre, null))
    }

    fun getGenreTag(genreName: String, genreCode: String): View {
        with(binding.genre) {
            text = genreName
            maxLines = 1
            val newLayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            newLayoutParams.setMargins(8, 8, 8, 8)
            layoutParams = newLayoutParams
        }
        binding.root.downEffect().setOnClickListener {
            actionListener.genreOnClick(title = genreCode)
        }
        return binding.root
    }

}

interface GenreOnClickListener {
    fun genreOnClick(title: String)
}