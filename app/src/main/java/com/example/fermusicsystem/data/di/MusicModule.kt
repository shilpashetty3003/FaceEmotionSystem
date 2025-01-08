package com.example.fermusicsystem.data.di

import android.app.Application
import androidx.room.Room
import com.example.fermusicsystem.data.api.MusicAPI
import com.example.fermusicsystem.data.db.AppDatabase
import com.example.fermusicsystem.data.db.EmotionHistoryDao
import com.example.fermusicsystem.data.repository.MusicService
import com.example.fermusicsystem.data.repository.MusicServiceImpl
import com.example.fermusicsystem.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object MusicModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    fun provideDatabase( app: Application):AppDatabase =
        Room.databaseBuilder(app,AppDatabase::class.java,"music_db").allowMainThreadQueries().build()

    @Provides
    fun provideWallpaperDao(appDB: AppDatabase):EmotionHistoryDao =
        appDB.emotionHistoryDao()


    @Singleton
    @Provides
    fun provideMusicAPI(retrofit: Retrofit):MusicAPI{
        return retrofit.create(MusicAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideMusicService(musicApi :MusicAPI,appDB:AppDatabase) :MusicService{
        return MusicServiceImpl(musicApi,appDB)
    }




}