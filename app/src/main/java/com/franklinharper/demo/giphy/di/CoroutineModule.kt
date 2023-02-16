package com.franklinharper.demo.giphy.di

import com.franklinharper.demo.giphy.coroutine.CoroutineDispatchers
import com.franklinharper.demo.giphy.coroutine.CoroutineDispatchersImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoroutineModule {

    @Binds
    @Singleton
    abstract fun bindCoroutineDispatchers(
        coroutineDispatchersImpl: CoroutineDispatchersImpl,
    ): CoroutineDispatchers

}
