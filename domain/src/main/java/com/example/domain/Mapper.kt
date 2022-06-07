package com.example.domain

interface Mapper<From, To> {

    fun map(from: From): To
}