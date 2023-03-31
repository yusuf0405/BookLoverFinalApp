package com.example.domain.repository

import com.example.domain.models.UserDomain
import kotlinx.coroutines.flow.Flow

interface UserCacheRepository {

    fun fetchCurrentUserFromCacheFlow(): Flow<UserDomain>

    fun fetchCurrentUserFromCache(): UserDomain

    suspend fun saveCurrentUserFromCache(newUser: UserDomain): Boolean
}