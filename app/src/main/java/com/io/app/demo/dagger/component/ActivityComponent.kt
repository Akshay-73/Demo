package com.io.app.demo.dagger.component

import android.app.Activity
import com.io.app.demo.dagger.ActivityScope
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface ActivityComponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: Activity): Builder

        fun build(): ActivityComponent
    }

}