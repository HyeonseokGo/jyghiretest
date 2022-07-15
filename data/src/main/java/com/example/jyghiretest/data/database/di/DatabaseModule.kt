package com.example.jyghiretest.data.database.di

import android.content.Context
import androidx.room.Room
import com.example.jyghiretest.data.database.JygDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideJygDatabase(
        @ApplicationContext context: Context
    ): JygDatabase {
        return Room.databaseBuilder(
            context,
            JygDatabase::class.java, JygDatabase.NAME
        ).build()
    }


    @Provides
    @Singleton
    fun provideProductDao(
        jygDatabase: JygDatabase
    ) = jygDatabase.productDao()


    @Provides
    @Singleton
    fun provideCategoryDao(
        jygDatabase: JygDatabase
    ) = jygDatabase.categoryDao()


}
