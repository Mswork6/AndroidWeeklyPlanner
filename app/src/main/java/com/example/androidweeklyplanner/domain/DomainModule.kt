package com.example.androidweeklyplanner.domain

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import androidx.work.WorkManager
import com.example.androidweeklyplanner.domain.interactor.notification.NotificationInteractor
import com.example.androidweeklyplanner.domain.interactor.notification.impl.NotificationInteractorImpl
import com.example.androidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.androidweeklyplanner.domain.interactor.saver.TaskInteractorImpl
import com.example.androidweeklyplanner.domain.repository.FilterRepository
import com.example.androidweeklyplanner.domain.repository.SortRepository
import com.example.androidweeklyplanner.domain.repository.TaskRepository
import com.example.androidweeklyplanner.domain.repository.WeekRepository
import com.example.androidweeklyplanner.domain.repository.impl.FilterRepositoryImpl
import com.example.androidweeklyplanner.domain.repository.impl.SortRepositoryImpl
import com.example.androidweeklyplanner.domain.repository.impl.TaskRepositoryImpl
import com.example.androidweeklyplanner.domain.repository.impl.WeekRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScreenSortRepo

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FilterScreenSortRepo

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {
    @Binds
    fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    fun bindWeekRepository(impl: WeekRepositoryImpl): WeekRepository

    @Binds
    fun bindFilterRepository(impl: FilterRepositoryImpl): FilterRepository

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
        @Singleton
        fun provideWorkManager(
            @ApplicationContext context: Context
        ): WorkManager = WorkManager.getInstance(context)

        @MainScreenSortRepo
        @Singleton
        @Provides
        fun provideMainScreenSortRepository(): SortRepository = SortRepositoryImpl()

        @FilterScreenSortRepo
        @Singleton
        @Provides
        fun provideFilterScreenSortRepository(): SortRepository = SortRepositoryImpl()

        @Provides
        fun provideScope(): CoroutineScope = CoroutineScope(SupervisorJob())
    }
}

