package com.dev.town.small.brewtopia.DataClass;

import android.util.Log;

/**
 * Created by Andrew on 3/1/2015.
 */
public class CurrentUser {

    private UserSchema user;

    // Log cat tag
    private static final String LOG = "CurrentUser";

    //Singleton
    private static CurrentUser mInstance = null;

    public static CurrentUser getInstance() {
        if (mInstance == null) {
            mInstance = new CurrentUser();
        }
        return mInstance;
    }
    // constructor
    private CurrentUser() {
    }


    public UserSchema getUser() {
        return user;
    }

    public void setUser(UserSchema user) {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: setUser");
        this.user = user;
    }
}
