package com.tantra.tantrayoga.common.dependencyinjection.component

import dagger.Component
import com.tantra.tantrayoga.common.dependencyinjection.module.NetworkModule
import com.tantra.tantrayoga.ui.post.PostListViewModel
import com.tantra.tantrayoga.ui.post.PostViewModel
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(NetworkModule::class)]) //bridge between @Inject and @Module
interface ViewModelComponent {
    /**
     * Injects required dependencies into the specified PostListViewModel.
     * @param postListViewModel PostListViewModel in which to inject the dependencies
     */
    fun inject(postListViewModel: PostListViewModel)
    /**
     * Injects required dependencies into the specified PostViewModel.
     * @param postViewModel PostViewModel in which to inject the dependencies
     */
    fun inject(postViewModel: PostViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelComponent

        fun networkModule(networkModule: NetworkModule): Builder
    }
}