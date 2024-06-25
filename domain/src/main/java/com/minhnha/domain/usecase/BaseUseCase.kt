package com.minhnha.domain.usecase

abstract class BaseUseCase<in Params, out Type> {
    suspend operator fun invoke(parameters: Params): Type {
        return execute(parameters)
    }

    protected abstract suspend fun execute(parameters: Params): Type
}