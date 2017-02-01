package com.team4runner.forrunner;

import android.app.Application;

import com.appjolt.sdk.Appjolt;

/**
 * Created by Lucas on 05/09/2016.
 */
public class ForRunnerAppClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Appjolt - Init SDK
        // Add to the main Application onCreate() event.
        Appjolt.init(this);
    }
}
