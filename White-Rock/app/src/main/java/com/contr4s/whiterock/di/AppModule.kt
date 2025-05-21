package com.contr4s.whiterock.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.contr4s.whiterock.data.repository.*
import com.contr4s.whiterock.domain.usecase.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideGetFriendsUseCase(repository: FriendsRepository): GetFriendsUseCase {
        return GetFriendsUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideGetGymsUseCase(repository: GymsRepository): GetGymsUseCase {
        return GetGymsUseCase(repository)
    }
}
