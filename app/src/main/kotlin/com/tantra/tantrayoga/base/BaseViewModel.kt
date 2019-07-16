package com.tantra.tantrayoga.base

import android.arch.lifecycle.ViewModel
import com.tantra.tantrayoga.common.dependencyinjection.component.ApplicationComponent
import com.tantra.tantrayoga.common.dependencyinjection.component.DaggerApplicationComponent
import com.tantra.tantrayoga.common.dependencyinjection.component.DaggerViewModelComponent
import com.tantra.tantrayoga.common.dependencyinjection.component.ViewModelComponent
import com.tantra.tantrayoga.common.dependencyinjection.module.ApplicationModule
import com.tantra.tantrayoga.common.dependencyinjection.module.NetworkModule
import com.tantra.tantrayoga.common.dependencyinjection.module.RoomModule
import com.tantra.tantrayoga.ui.post.PostListViewModel
import com.tantra.tantrayoga.ui.post.PostViewModel

abstract class BaseViewModel : ViewModel() {
    private val injector: ViewModelComponent = DaggerViewModelComponent
        .builder()
        .networkModule(NetworkModule)
        .build()
//    private val appInjector: ApplicationComponent = DaggerApplicationComponent
//        .builder()
//        .applicationModule(ApplicationModule(getApplication()))
//        .roomModule(RoomModule(getApplication()))
//        .build()

//    private val component: ViewModelComponent = DaggerApplicationComponent
//            .builder()
//            .networkModule(NetworkModule)
//            .build()


    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is PostListViewModel -> {
                injector.inject(this)
//                appInjector.inject(this)
            }
            is PostViewModel -> {
                injector.inject(this)
//                appInjector.inject(this)
            }
        }
    }
}