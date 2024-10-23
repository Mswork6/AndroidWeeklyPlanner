package com.example.courseworkandroidweeklyplanner.domain

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import com.example.courseworkandroidweeklyplanner.domain.interactor.notification.NotificationInteractor
import com.example.courseworkandroidweeklyplanner.domain.interactor.notification.impl.NotificationInteractorImpl
import com.example.courseworkandroidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.courseworkandroidweeklyplanner.domain.interactor.saver.TaskInteractorImpl
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {
    @Binds
    fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    fun bindWeekRepository(impl: WeekRepositoryImpl): WeekRepository

    @Binds
    fun bindSortRepository(impl: SortRepositoryImpl): SortRepository

    @Binds
    fun bindTaskInteractor(impl: TaskInteractorImpl): TaskInteractor

    @Binds
    fun bindNotificationInteractor(impl: NotificationInteractorImpl): NotificationInteractor

    companion object {
        @Provides
        @Singleton
        fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
            return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }

        @Provides
        @Singleton
        fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
            return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        @Provides
        fun provideScope(): CoroutineScope = CoroutineScope(SupervisorJob())
    }
}