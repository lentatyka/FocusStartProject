package com.lentatyka.focusstartproject.di

import com.google.gson.JsonDeserializer
import com.lentatyka.focusstartproject.data.network.ExchangeRatesApi
import com.lentatyka.focusstartproject.data.network.RateRepositoryImpl
import com.lentatyka.focusstartproject.data.network.model.ExchangeRateDeserializer
import com.lentatyka.focusstartproject.data.network.model.ExchangeRatesDto
import com.lentatyka.focusstartproject.domain.network.RateRepository
import com.lentatyka.focusstartproject.temp.FakeRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
abstract class FakeNetworkModule {
//    @Binds
//    abstract override fun  bindJsonDeserializer(
//        serializer: ExchangeRateDeserializer
//    ): JsonDeserializer<@JvmSuppressWildcards ExchangeRatesDto>
//

    @Binds
    abstract fun bindRateRepository(repository: FakeRepo): RateRepository

    companion object{

        @Provides
        @Singleton
        fun provideFakeExchangeRatesApi():ExchangeRatesApi{
            return Mockito.mock(ExchangeRatesApi::class.java)
        }
    }
}