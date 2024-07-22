package com.example.nbc_assignment.di

import android.content.Context
import com.example.nbc_assignment.model.Item
import com.example.nbc_assignment.repository.DataRepository
import com.example.nbc_assignment.utils.ItemDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
    fun provideJsonRepository(@ApplicationContext context: Context, gson: Gson): DataRepository {
        return DataRepository(context, gson)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Item::class.java, ItemDeserializer())
            .create()
    }
}