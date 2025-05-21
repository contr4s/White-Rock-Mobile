package com.contr4s.whiterock.di

import com.contr4s.whiterock.data.repository.FriendsRepository
import com.contr4s.whiterock.data.repository.FriendsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FriendsModule {
    @Binds
    @Singleton
    abstract fun bindFriendsRepository(
        impl: FriendsRepositoryImpl
    ): FriendsRepository
}
