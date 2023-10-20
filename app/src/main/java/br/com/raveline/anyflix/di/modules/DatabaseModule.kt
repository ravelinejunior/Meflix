package br.com.raveline.anyflix.di.modules

import android.content.Context
import androidx.room.Room
import br.com.raveline.anyflix.data.database.AnyflixDatabase
import br.com.raveline.anyflix.data.database.dao.MovieDao
import br.com.raveline.anyflix.utils.databaseName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AnyflixDatabase {
        return Room.databaseBuilder(
            context,
            AnyflixDatabase::class.java,
            databaseName
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMovieDao(db: AnyflixDatabase): MovieDao {
        return db.movieDao()
    }

}