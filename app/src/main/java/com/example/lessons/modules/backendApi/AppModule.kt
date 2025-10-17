package com.example.lessons.modules.backendApi

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(@ApplicationContext context: Context): ApiService {
        val apiService = Api(context)
        return apiService.provideApiServiceWithBearer()
    }
}