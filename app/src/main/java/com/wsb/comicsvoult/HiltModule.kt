package com.wsb.comicsvoult

import android.content.Context
import androidx.room.Room
import com.wsb.comicsvoult.model.api.ApiService
import com.wsb.comicsvoult.model.api.MarvelApiRepo
import com.wsb.comicsvoult.model.connectivity.ConnectivityMonitor
import com.wsb.comicsvoult.model.db.CharacterDao
import com.wsb.comicsvoult.model.db.CollectionDb
import com.wsb.comicsvoult.model.db.CollectionDbRepo
import com.wsb.comicsvoult.model.db.CollectionDbRepoImpl
import com.wsb.comicsvoult.model.db.Constants.DB
import com.wsb.comicsvoult.model.db.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule() {
    @Provides
    fun provideApiRepo() = MarvelApiRepo(ApiService.api)

    @Provides
    fun provideCollectionDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CollectionDb::class.java, DB).build()

    @Provides
    fun providesCharacterDao(collectionDb: CollectionDb) = collectionDb.characterDao()

    @Provides
    fun providesNoteDao(collectionDb: CollectionDb) = collectionDb.noteDao()

    @Provides
    fun provideDbRepoImpl(characterDao: CharacterDao, noteDao: NoteDao): CollectionDbRepo =
        CollectionDbRepoImpl(characterDao, noteDao)

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context) =
        ConnectivityMonitor.getInstance(context)

}