package com.lvs.data.converters

abstract class BaseConverter<in T, out E> {
    abstract fun convert(from: T): E

}