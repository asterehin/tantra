package com.tantra.tantrayoga

import android.app.Application
import com.tantra.tantrayoga.common.dependencyinjection.component.ApplicationComponent
import com.tantra.tantrayoga.common.dependencyinjection.module.ApplicationModule
import com.tantra.tantrayoga.common.dependencyinjection.module.RoomModule

class TantraApplication : Application() {

    private val mApplicationComponent: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()

    }

    /**
     * @return application injector of type [ApplicationComponent]
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
