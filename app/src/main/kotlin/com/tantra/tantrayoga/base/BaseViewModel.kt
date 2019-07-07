package com.tantra.tantrayoga.base

import android.arch.lifecycle.ViewModel
import net.gahfy.mvvmposts.injection.component.ViewModelInjector
import net.gahfy.mvvmposts.injection.module.NetworkModule
import com.tantra.tantrayoga.ui.post.PostListViewModel
import com.tantra.tantrayoga.ui.post.PostViewModel
import net.gahfy.mvvmposts.injection.component.DaggerViewModelInjector

abstract class BaseViewModel:ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is PostListViewModel -> injector.inject(this)
            is PostViewModel -> injector.inject(this)
        }
    }
}