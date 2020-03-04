package com.example.newyorktimes.dagger.module

import com.example.newyorktimes.BuildConfig
import com.example.newyorktimes.api.ApiService
import com.example.newyorktimes.api.ArticleDataFactory
import com.example.newyorktimes.api.ArticleDataSource
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val NYT_BASE_URL = "https://api.nytimes.com/ "
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpBuilder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val interceptor = Interceptor { chain ->
            val original = chain.request()
            val url: HttpUrl = original.url().newBuilder()
                .addQueryParameter("api-key", BuildConfig.NYT_API_KEY)
                .build()

            val request = original.newBuilder().url(url).build()
            chain.proceed(request)
        }
        val newBuilder = httpBuilder.build().newBuilder().addInterceptor(httpLoggingInterceptor)
            .addInterceptor(interceptor)
        return newBuilder.build()
    }

    @Singleton
    @Provides
    fun providesRetrofitService(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(NYT_BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): ApiService {
        return retrofit.create<ApiService>(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideArticleDataSource(apiService: ApiService): ArticleDataSource {
        return ArticleDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideArticleDataFactory(articleDataSource: ArticleDataSource): ArticleDataFactory {
        return ArticleDataFactory(articleDataSource)
    }
}