package com.io.app.demo.dagger.component


import android.app.Application
import com.io.app.demo.application.MainApp
import com.io.app.demo.dagger.module.AppModule
import com.io.app.demo.dagger.module.RestModule
import com.io.app.demo.dagger.module.ViewModelModule
import com.io.app.demo.ui.MainActivity

import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, ViewModelModule::class, RestModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(application: MainApp) : MainApp
    fun inject(baseActivity: MainActivity) : MainActivity

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder?
        fun build(): ApplicationComponent

    }


}