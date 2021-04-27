package com.io.app.demo.dagger.module


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.io.app.demo.dagger.factory.ViewModelKey
import com.io.app.demo.dagger.factory.ViewModelProvideFactory
import com.io.app.demo.ui.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule{
     @Binds
     abstract fun bindViewModelFactory(baseViewModelFactory: ViewModelProvideFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindmainVM(mainVM: MainViewModel) : ViewModel
}
