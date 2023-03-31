package com.joseph.common_api

interface Mapper<From, To> {

    fun map(from: From): To
}