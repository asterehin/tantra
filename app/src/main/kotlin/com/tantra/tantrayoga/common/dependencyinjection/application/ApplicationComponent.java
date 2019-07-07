package com.tantra.tantrayoga.common.dependencyinjection.application;


import dagger.Component;

@ApplicationScope
@Component(
        modules = {
                ApplicationModule.class,
                SettingsModule.class
        }
)
public interface ApplicationComponent {

//    ControllerComponent newControllerComponent(
//            ControllerModule controllerModule,
//            ViewMvcModule viewMvcModule);
//
//    ServiceComponent newServiceComponent(ServiceModule serviceModule);

}