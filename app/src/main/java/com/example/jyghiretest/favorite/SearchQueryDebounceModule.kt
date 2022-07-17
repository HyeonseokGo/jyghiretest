package com.example.jyghiretest.favorite

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object SearchQueryDebounceModule {

    @Provides
    @ViewModelScoped
    @Named("SearchQueryDebounce")
    fun provideSearchQueryDebounce() = 200L

}
