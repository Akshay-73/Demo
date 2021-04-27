package com.io.app.demo.dagger.module

import androidx.lifecycle.ViewModelProvider
import com.io.app.demo.dagger.factory.ViewModelProvideFactory
import dagger.Binds
import dagger.Module

@Module(includes = [ViewModelModule::class])
abstract class ActivityModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProvideFactory): ViewModelProvider.Factory

}