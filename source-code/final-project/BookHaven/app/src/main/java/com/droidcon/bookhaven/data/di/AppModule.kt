package com.droidcon.bookhaven.data.di

import android.app.Application
import androidx.room.Room
import com.droidcon.bookhaven.data.repository.BookRepository
import com.droidcon.bookhaven.data.repository.BookRepositoryImpl
import com.droidcon.bookhaven.data.source.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideBookDatabase(app: Application): BookDatabase = Room.databaseBuilder(app, BookDatabase::class.java, BookDatabase.DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideBookRepository(db: BookDatabase): BookRepository = BookRepositoryImpl(db.bookDao)

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
