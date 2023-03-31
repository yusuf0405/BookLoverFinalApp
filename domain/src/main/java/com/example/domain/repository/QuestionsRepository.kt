package com.example.domain.repository

interface QuestionsRepository {

   suspend fun fetchAllQuestionsPosters(): List<String>
}