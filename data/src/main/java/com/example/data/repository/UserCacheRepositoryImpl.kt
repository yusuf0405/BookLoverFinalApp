package com.example.data.repository

import android.content.Context
import com.example.data.models.UserSaveModel
import com.example.domain.Mapper
import com.example.domain.models.UserDomain
import com.example.domain.repository.UserCacheRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserCacheRepositoryImpl(
    private val context: Context,
    private val mapperToDomain: Mapper<UserSaveModel, UserDomain>,
    private val mapperToSaveModel: Mapper<UserDomain, UserSaveModel>
) : UserCacheRepository {

    private companion object {
        const val CURRENT_STUDENT_EDITOR_SAVE_KEY = "CURRENT_EDITOR_STUDENT_SAVE_KEY"
        const val CURRENT_STUDENT_SAVE_KEY = "CURRENT_STUDENT_SAVE_KEY"
    }

    override fun fetchCurrentUserFromCache(): Flow<UserDomain> = flow {
        val pref =
            context.getSharedPreferences(CURRENT_STUDENT_EDITOR_SAVE_KEY, Context.MODE_PRIVATE)
        val userSaveModel = Gson()
            .fromJson(pref.getString(CURRENT_STUDENT_SAVE_KEY, null), UserSaveModel::class.java)
            ?: UserSaveModel.unknown()
        emit(mapperToDomain.map(userSaveModel))
    }

    override suspend fun saveCurrentUserFromCache(newUser: UserDomain) = context
        .getSharedPreferences(CURRENT_STUDENT_EDITOR_SAVE_KEY, Context.MODE_PRIVATE)
        .edit()
        .putString(CURRENT_STUDENT_SAVE_KEY, Gson().toJson(mapperToSaveModel.map(newUser)))
        .commit()
}