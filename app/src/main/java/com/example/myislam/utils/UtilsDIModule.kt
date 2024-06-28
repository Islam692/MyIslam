package com.example.myislam.utils

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UtilsDIModule {

    @Provides
    fun provideUtils(@ApplicationContext context: Context): Utils {
        return Utils(context)
    }
}
