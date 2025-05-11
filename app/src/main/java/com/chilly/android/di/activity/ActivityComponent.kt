package com.chilly.android.di.activity

import androidx.activity.ComponentActivity
import com.chilly.android.di.application.ApplicationComponent
import com.chilly.android.di.application.ApplicationExposedDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@ActivityScope
@Component(
    modules = [ActivityModule::class],
    dependencies = [ApplicationComponent::class]
)
interface ActivityComponent : ApplicationExposedDependencies, ActivityExposedDependencies {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: ComponentActivity): Builder
        fun parent(component: ApplicationComponent): Builder
        fun build(): ActivityComponent
    }
}

@Scope
annotation class ActivityScope