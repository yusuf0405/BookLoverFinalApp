package com.example.bookloverfinalapp.app.utils.pref

import android.app.Activity
import android.content.Context
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.utils.cons.CURRENT_EDITOR_STUDENT_SAVE_KEY
import com.example.bookloverfinalapp.app.utils.cons.CURRENT_STUDENT_SAVE_KEY
import com.google.gson.Gson


class CurrentUser {

    fun saveCurrentUser(
        user: User,
        activity: Activity,
    ) {
        activity.getSharedPreferences(CURRENT_EDITOR_STUDENT_SAVE_KEY, Context.MODE_PRIVATE)
            .edit()
            .putString(CURRENT_STUDENT_SAVE_KEY, Gson().toJson(user))
            .apply()
    }

    fun getCurrentUser(
        activity: Activity,
    ): User {
        val pref =
            activity.getSharedPreferences(CURRENT_EDITOR_STUDENT_SAVE_KEY, Context.MODE_PRIVATE)
        return Gson().fromJson(pref.getString(CURRENT_STUDENT_SAVE_KEY, "")!!, User::class.java)
    }
}