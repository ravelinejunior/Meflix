package br.com.raveline.anyflix.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.raveline.anyflix.data.database.dao.MovieDao
import br.com.raveline.anyflix.data.database.entities.MovieEntity

@Database(
    version = 1,
    entities = [MovieEntity::class],
)
abstract class AnyflixDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}