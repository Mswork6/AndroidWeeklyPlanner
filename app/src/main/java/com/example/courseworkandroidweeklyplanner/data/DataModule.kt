package com.example.courseworkandroidweeklyplanner.data

import android.content.Context
import androidx.room.Room
import com.example.courseworkandroidweeklyplanner.data.dao.TaskDao
import com.example.courseworkandroidweeklyplanner.domain.repository.SortRepository
import com.example.courseworkandroidweeklyplanner.domain.repository.TaskRepository
import com.example.courseworkandroidweeklyplanner.domain.repository.WeekRepository
import com.example.courseworkandroidweeklyplanner.domain.repository.impl.SortRepositoryImpl
import com.example.courseworkandroidweeklyplanner.domain.repository.impl.TaskRepositoryImpl
import com.example.courseworkandroidweeklyplanner.domain.repository.impl.WeekRepositoryImpl
import dagger.Binds
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
    }
}
