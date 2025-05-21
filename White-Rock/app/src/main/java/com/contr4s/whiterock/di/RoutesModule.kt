package com.contr4s.whiterock.di

import com.contr4s.whiterock.data.repository.RoutesRepository
import com.contr4s.whiterock.data.repository.RoutesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.contr4s.whiterock.domain.usecase.GetRoutesUseCase
import com.contr4s.whiterock.domain.usecase.GetRouteDetailsUseCase

@Module
@InstallIn(SingletonComponent::class)
object RoutesModule {
    
    @Provides
    @Singleton
    fun provideRoutesRepository(): RoutesRepository {
        return RoutesRepositoryImpl()
    }
    
    @Provides
    @Singleton
    fun provideGetRoutesUseCase(repository: RoutesRepository): GetRoutesUseCase {
        return GetRoutesUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideGetRouteDetailsUseCase(repository: RoutesRepository): GetRouteDetailsUseCase {
        return GetRouteDetailsUseCase(repository)
    }
}
