package com.tantra.tantrayoga.base

import androidx.lifecycle.ViewModel
import com.tantra.tantrayoga.common.dependencyinjection.component.DaggerViewModelComponent
import com.tantra.tantrayoga.common.dependencyinjection.component.ViewModelComponent
import com.tantra.tantrayoga.common.dependencyinjection.module.ApplicationModule
import com.tantra.tantrayoga.common.dependencyinjection.module.NetworkModule
import com.tantra.tantrayoga.ui.programm.ProgrammListViewModel

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
            is ProgrammListViewModel -> {
                injector.inject(this)
//                appInjector.inject(this)
            }
        }
    }
}