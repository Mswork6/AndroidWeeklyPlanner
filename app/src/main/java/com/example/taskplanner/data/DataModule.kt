package com.example.taskplanner.data

import android.content.Context
import androidx.room.Room
import com.example.taskplanner.data.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    companion object {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): ApplicationDatabase = Room
            .databaseBuilder(
                context = context,
                klass = ApplicationDatabase::class.java,
                name = "android_weekly_planner_database"
            ).fallbackToDestructiveMigration()
            .build()

        @Provides
        @Singleton
        fun provideTaskDao(database: ApplicationDatabase): TaskDao = database.taskDao()

        @Provides
        @Singleton
        fun provideCelebratedDatesDataStore(
            @ApplicationContext context: Context
        ): CelebratedDatesDataStore =
            CelebratedDatesDataStore(context)
    }
}
