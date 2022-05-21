package com.example.bookloverfinalapp.app.utils

import android.content.Context
import com.example.bookloverfinalapp.R

object GenresManager {

    fun getAllGenres(context: Context): MutableList<String> {
        val allBookGenresList = mutableListOf<String>()
        context.apply {
            allBookGenresList.apply {
                add(getString(R.string.criminal))
                add(getString(R.string.secular))
                add(getString(R.string.historical))
                add(getString(R.string.science_fiction))
                add(getString(R.string.fantasy))
                add(getString(R.string.biography))
                add(getString(R.string.adventures))
                add(getString(R.string.historical_novel))
                add(getString(R.string.a_love_affair))
                add(getString(R.string.adventure_novel))
                add(getString(R.string.scientific_works))
                add(getString(R.string.articles))
                add(getString(R.string.epic))
                add(getString(R.string.legends))
                add(getString(R.string.fairy_tales))
                add(getString(R.string.jokes))
                add(getString(R.string.poetry))
                add(getString(R.string.prose))
                add(getString(R.string.childrens_book))
                add(getString(R.string.biography))
                add(getString(R.string.journalism))
                add(getString(R.string.military_affairs))
                add(getString(R.string.business))
                add(getString(R.string.politics))
                add(getString(R.string.finance))
                add(getString(R.string.economy))
                add(getString(R.string.religious_literature))
                add(getString(R.string.methodical_manual))
                add(getString(R.string.textbook))
                add(getString(R.string.psychology))
                add(getString(R.string.hobby))
                add(getString(R.string.foreign_literature))
                add(getString(R.string.russian_literature))
            }

        }
        return allBookGenresList
    }

    fun getAllGenresCode(): MutableList<Int> {
        val allBookGenresCodeList = mutableListOf<Int>()
        allBookGenresCodeList.apply {
            add(R.string.criminal)
            add(R.string.secular)
            add(R.string.historical)
            add(R.string.science_fiction)
            add(R.string.fantasy)
            add(R.string.biography)
            add(R.string.adventures)
            add(R.string.historical_novel)
            add(R.string.a_love_affair)
            add(R.string.adventure_novel)
            add(R.string.scientific_works)
            add(R.string.articles)
            add(R.string.epic)
            add(R.string.legends)
            add(R.string.fairy_tales)
            add(R.string.jokes)
            add(R.string.poetry)
            add(R.string.prose)
            add(R.string.childrens_book)
            add(R.string.biography)
            add(R.string.journalism)
            add(R.string.military_affairs)
            add(R.string.business)
            add(R.string.politics)
            add(R.string.finance)
            add(R.string.economy)
            add(R.string.religious_literature)
            add(R.string.methodical_manual)
            add(R.string.textbook)
            add(R.string.psychology)
            add(R.string.hobby)
            add(R.string.foreign_literature)
            add(R.string.russian_literature)
        }
        return allBookGenresCodeList
    }

    fun checkGenre(genreId: Int): String =
        when (genreId) {
            R.string.criminal -> "criminal"
            R.string.secular -> "secular"
            (R.string.historical) -> "historical"
            (R.string.science_fiction) -> "science_fiction"
            (R.string.fantasy) -> "fantasy"
            (R.string.biography) -> "biography"
            (R.string.adventures) -> "adventures"
            (R.string.historical_novel) -> "historical_novel"
            (R.string.a_love_affair) -> "a_love_affair"
            (R.string.adventure_novel) -> "adventure_novel"
            (R.string.scientific_works) -> "scientific_works"
            (R.string.articles) -> "articles"
            (R.string.epic) -> "epic"
            (R.string.legends) -> "legends"
            (R.string.fairy_tales) -> "fairy_tales"
            (R.string.jokes) -> "jokes"
            (R.string.poetry) -> "poetry"
            (R.string.prose) -> "prose"
            (R.string.childrens_book) -> "childrens_book"
            (R.string.biography) -> "biography"
            (R.string.journalism) -> "journalism"
            (R.string.military_affairs) -> "military_affairs"
            (R.string.business) -> "business"
            (R.string.politics) -> "politics"
            (R.string.finance) -> "finance"
            (R.string.economy) -> "economy"
            (R.string.religious_literature) -> "religious_literature"
            (R.string.methodical_manual) -> "methodical_manual"
            (R.string.textbook) -> "textbook"
            (R.string.psychology) -> "psychology"
            (R.string.hobby) -> "hobby"
            (R.string.foreign_literature) -> "foreign_literature"
            else -> "russian_literature"
        }


    fun getGenreByCloudCode(context: Context, code: String): String {
        var genre: String
        context.apply {
            genre = when (code) {
                "criminal" -> getString(R.string.criminal)
                "secular" -> getString(R.string.secular)
                "historical" -> getString(R.string.historical)
                "science_fiction" -> getString(R.string.science_fiction)
                "fantasy" -> getString(R.string.fantasy)
                "biography" -> getString(R.string.biography)
                "adventures" -> getString(R.string.adventures)
                "historical_novel" -> getString(R.string.historical_novel)
                "a_love_affair" -> getString(R.string.a_love_affair)
                "adventure_novel" -> getString(R.string.adventure_novel)
                "scientific_works" -> getString(R.string.scientific_works)
                "articles" -> getString(R.string.articles)
                "epic" -> getString(R.string.epic)
                "legends" -> getString(R.string.legends)
                "fairy_tales" -> getString(R.string.fairy_tales)
                "jokes" -> getString(R.string.jokes)
                "poetry" -> getString(R.string.poetry)
                "prose" -> getString(R.string.prose)
                "childrens_book" -> getString(R.string.childrens_book)
                "journalism" -> getString(R.string.journalism)
                "military_affairs" -> getString(R.string.military_affairs)
                "business" -> getString(R.string.business)
                "politics" -> getString(R.string.politics)
                "finance" -> getString(R.string.finance)
                "economy" -> getString(R.string.economy)
                "religious_literature" -> getString(R.string.religious_literature)
                "methodical_manual" -> getString(R.string.methodical_manual)
                "textbook" -> getString(R.string.textbook)
                "psychology" -> getString(R.string.psychology)
                "hobby" -> getString(R.string.hobby)
                "foreign_literature" -> getString(R.string.foreign_literature)
                else -> getString(R.string.russian_literature)
            }
        }
        return genre
    }

}
