package com.example.data.repository.user_cache

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

    override fun fetchCurrentUserFromCache(): UserDomain = mapperToDomain.map(fetchCachedUser())

    override fun fetchCurrentUserFromCacheFlow(): Flow<UserDomain> = flow {
        emit(mapperToDomain.map(fetchCachedUser()))
    }

    private fun fetchCachedUser(): UserSaveModel {
        val pref =
            context.getSharedPreferences(CURRENT_STUDENT_EDITOR_SAVE_KEY, Context.MODE_PRIVATE)
        return Gson()
            .fromJson(pref.getString(CURRENT_STUDENT_SAVE_KEY, null), UserSaveModel::class.java)
            ?: UserSaveModel.unknown()

    }

    override suspend fun saveCurrentUserFromCache(newUser: UserDomain) = context
        .getSharedPreferences(CURRENT_STUDENT_EDITOR_SAVE_KEY, Context.MODE_PRIVATE)
        .edit()
        .putString(CURRENT_STUDENT_SAVE_KEY, Gson().toJson(mapperToSaveModel.map(newUser)))
        .commit()
}