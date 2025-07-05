package com.iskan.chroniclelens.di

import com.iskan.chroniclelens.data.repository.JournalRepositoryImpl
import com.iskan.chroniclelens.domain.repository.JournalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJournalRepository(): JournalRepository {
        return JournalRepositoryImpl()
    }
}
