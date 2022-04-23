package com.example.domain.domain

abstract class Mapper<From, To> {

    abstract fun map(from: From): To

}