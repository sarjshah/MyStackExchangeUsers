package com.practice.mystackexchangeusers.di

import com.practice.mystackexchangeusers.common.Constants
import com.practice.mystackexchangeusers.data.remote.StackExchangeApi
import com.practice.mystackexchangeusers.data.repository.UserRepositoryImpl
import com.practice.mystackexchangeusers.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApuService() = Retrofit.Builder()
        .baseUrl(Constants.STACK_EXCHANGE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(StackExchangeApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(serviceApi: StackExchangeApi): UserRepository =
        UserRepositoryImpl(serviceApi)
}