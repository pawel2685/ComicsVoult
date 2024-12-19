package com.wsb.comicsvoult.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DbCharacter::class, DbNote::class], version = 1, exportSchema = false)
abstract class CollectionDb: RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: CollectionDb? = null

        fun getInstance(context: Context): CollectionDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CollectionDb::class.java,
                    "collection_db"
                ).fallbackToDestructiveMigration()  // Add this line
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}