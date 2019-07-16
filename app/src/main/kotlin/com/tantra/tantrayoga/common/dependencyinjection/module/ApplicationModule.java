package com.tantra.tantrayoga.common.dependencyinjection.module;

import android.app.Application;
import com.tantra.tantrayoga.common.dependencyinjection.application.ApplicationScope;
import com.tantra.tantrayoga.common.dependencyinjection.logging.MyLogger;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application application() {
        return mApplication;
    }

//    @Provides
//    @ApplicationScope
//    MyLogger logger() {
//        return new MyLogger();
//    }

//    @Provides
//    @ApplicationScope
//    EventBusPoster eventBusPoster() {
//        return new EventBusPoster(EventBus.getDefault());
//    }
//
//    @Provides
//    @ApplicationScope
//    EventBusSubscriber eventBusSubscriber() {
//        return new EventBusSubscriber(EventBus.getDefault());
//    }
}
