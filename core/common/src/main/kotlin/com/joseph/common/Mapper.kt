package com.joseph.common

interface Mapper<From, To> {

    fun map(from: From): To
}