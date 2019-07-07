package com.tantra.tantrayoga;

import android.app.Application;
import android.support.annotation.UiThread;
import com.tantra.tantrayoga.common.dependencyinjection.application.ApplicationComponent;
import com.tantra.tantrayoga.common.dependencyinjection.application.ApplicationModule;
import com.tantra.tantrayoga.common.dependencyinjection.application.SettingsModule;

public class TantraApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
//        } else {
//            Timber.plant(new TimberReleaseTree());
//        }
    }

    /**
     * @return application injector of type {@link ApplicationComponent}
     */
//    @UiThread
//    public ApplicationComponent getApplicationComponent() {
//        if (mApplicationComponent == null) {
//            mApplicationComponent = DaggerApplicationComponent.builder()
//                    .applicationModule(new ApplicationModule(this))
//                    .settingsModule(new SettingsModule())
//                    .build();
//        }
//        return mApplicationComponent;
//    }
}
