package com.arvind.notewakeup.di

import android.app.Application
import com.arvind.notewakeup.storage.datastore.UIModeDataStore
import com.arvind.notewakeup.storage.db.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@InstallIn(ActivityComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun providePreferenceManager(application: Application): UIModeDataStore {
        return UIModeDataStore(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideNoteDatabase(application: Application): NoteDatabase {
        return NoteDatabase.invoke(application.applicationContext)
    }

}