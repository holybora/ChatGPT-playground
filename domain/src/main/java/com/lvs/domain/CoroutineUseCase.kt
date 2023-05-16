package com.lvs.domain

abstract class CoroutineUseCase<T, E> {
    abstract suspend operator fun invoke(params: T) : E

}