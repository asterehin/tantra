package com.tantra.tantrayoga.common.dependencyinjection.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import dagger.Module;
import dagger.Provides;

@Module
public class SettingsModule {

//    @Provides
//    @ApplicationScope
//    SettingsManager settingsManager(Application application) {
//        SharedPreferences sharedPreferences = application
//                .getSharedPreferences(SyncStateContract.Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
//        return new SettingsManager(new SharedPrefsSettingsEntryFactory(sharedPreferences));
//    }

}
