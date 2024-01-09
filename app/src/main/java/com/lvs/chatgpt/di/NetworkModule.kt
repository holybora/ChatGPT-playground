package com.lvs.chatgpt.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lvs.chatgpt.BuildConfig
import com.lvs.data.remote.api.OpenAIApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //TODO: move const to configuration bundle
    private const val DEFAULT_OPENAI_API = "https://api.openai.com/"


    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .setDateFormat(DateFormat.LONG)
            .create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(logger: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addNetworkInterceptor(
            Interceptor { chain ->
                val original = chain.request()
                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.APP_KEY}")
                chain.proceed(requestBuilder.build())
            })
        .addNetworkInterceptor(logger)
        .callTimeout(Duration.ofMinutes(2))
        .connectTimeout(Duration.ofMinutes(2))
        .readTimeout(Duration.ofMinutes(2))
        .writeTimeout(Duration.ofMinutes(2))
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(DEFAULT_OPENAI_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenAIService(retrofit: Retrofit): OpenAIApi =
        retrofit.create(OpenAIApi::class.java)


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().also {
        it.setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }
}