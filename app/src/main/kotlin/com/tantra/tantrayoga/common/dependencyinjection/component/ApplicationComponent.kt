package com.tantra.tantrayoga.common.dependencyinjection.component


import com.tantra.tantrayoga.common.dependencyinjection.application.ApplicationScope
import com.tantra.tantrayoga.common.dependencyinjection.module.ApplicationModule
import com.tantra.tantrayoga.common.dependencyinjection.module.RoomModule
import dagger.Component

import javax.inject.Singleton

@Singleton
@ApplicationScope
@Component(modules = [ApplicationModule::class, RoomModule::class]) //bridge between @Inject and @Module
interface ApplicationComponent {

    //    ControllerComponent newControllerComponent(
    //            ControllerModule controllerModule,
    //            ViewMvcModule viewMvcModule);
    //
    //    ServiceComponent newServiceComponent(ServiceModule serviceModule);
//    fun inject(mainActivity: PostListActivity)
//
//    fun inject(postListViewModel: PostListViewModel)
//    /**
//     * Injects required dependencies into the specified PostViewModel.
//     * @param postViewModel PostViewModel in which to inject the dependencies
//     */
//    fun inject(postViewModel: PostViewModel)
//
//    fun postDao(): PostDao
//
//    fun demoDatabase(): AppDatabase
//
//    fun postRepository(): PostRepository
//
//    fun application(): Application

}