package com.lentatyka.focusstartproject.di

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.lentatyka.focusstartproject.common.Constants
import com.lentatyka.focusstartproject.data.RateRepositoryImpl
import com.lentatyka.focusstartproject.data.network.ExchangeRatesApi
import com.lentatyka.focusstartproject.data.network.model.ExchangeRateDeserializer
import com.lentatyka.focusstartproject.data.network.model.ExchangeRatesDto
import com.lentatyka.focusstartproject.domain.RateRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
abstract class NetworkModule {

    @Binds
    abstract fun  bindJsonDeserializer(
        serializer: ExchangeRateDeserializer
    ): JsonDeserializer<@JvmSuppressWildcards ExchangeRatesDto>

    @Binds
    abstract fun bindRateRepository(repository: RateRepositoryImpl):RateRepository

    companion object{
        @Provides
        @Singleton
        fun provideJsonType():Type{
            return ExchangeRatesDto::class.java
        }
        @Provides
        @Singleton
        fun provideGsonConverter(
            type: Type, typeAdapter: JsonDeserializer<@JvmSuppressWildcards ExchangeRatesDto>
        ): GsonConverterFactory {
            val gson = GsonBuilder()
                .registerTypeAdapter(type, typeAdapter)
                .create()
            return GsonConverterFactory.create(gson)
        }

        @Provides
        @Singleton
        fun provideExchangeRatesApi(gsonConverter: GsonConverterFactory):ExchangeRatesApi{
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(gsonConverter)
                .build()
                .create(ExchangeRatesApi::class.java)
        }
    }
}