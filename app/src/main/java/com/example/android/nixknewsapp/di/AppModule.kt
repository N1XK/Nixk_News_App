package com.example.android.nixknewsapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.android.nixknewsapp.data.api.NewsApi
import com.example.android.nixknewsapp.data.model.ArticleDatabase
import com.example.android.nixknewsapp.utils.Constants.Companion.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    private fun provideOkHttpClient(context: Context): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .build()
            )
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
            .client(provideOkHttpClient(context))
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi =
        retrofit.create(NewsApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): ArticleDatabase =
        Room.databaseBuilder(app, ArticleDatabase::class.java, "article_database")
            .fallbackToDestructiveMigration()
            .build()
}