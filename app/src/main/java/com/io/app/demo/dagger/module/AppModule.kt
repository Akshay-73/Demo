package com.io.app.demo.dagger.module

import android.content.Context
import com.io.app.demo.application.MainApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [RestModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideApp(): MainApp = MainApp.getInstance()


    @Provides
    @Singleton
    fun provideContext(app: MainApp): Context =  app.applicationContext





}