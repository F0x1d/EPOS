package com.f0x1d.epos.network.repository.base

abstract class BaseRepository<T> {

    abstract suspend fun requestData(): T
    open fun updateValues(values: Array<Any>): BaseRepository<T> {
        return this
    }
}