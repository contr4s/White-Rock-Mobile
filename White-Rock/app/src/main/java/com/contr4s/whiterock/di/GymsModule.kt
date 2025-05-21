package com.contr4s.whiterock.di

import com.contr4s.whiterock.data.repository.GymsRepositoryImpl
import com.contr4s.whiterock.data.repository.GymsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GymsModule {
    @Binds
    @Singleton
    abstract fun bindGymsRepository(
        impl: GymsRepositoryImpl
    ): GymsRepository
}
