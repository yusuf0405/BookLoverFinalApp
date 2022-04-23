package com.example.bookloverfinalapp.app.base

abstract class Abstract {

    abstract class Object<T, M : Mapper> {

        abstract fun map(mapper: M): T
    }

    interface Mapper
}