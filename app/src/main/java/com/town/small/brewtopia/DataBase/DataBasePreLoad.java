package com.town.small.brewtopia.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.town.small.brewtopia.DataClass.APPUTILS;
import com.town.small.brewtopia.DataClass.UserSchema;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andrew on 7/23/2016.
 */
public class DataBasePreLoad {

    // Log cat tag
    private static final String LOG = "DataBasePreLoad";

    private static DataBaseManager dbm;

    //Singleton
    private static DataBasePreLoad mInstance = null;

    public static DataBasePreLoad getInstance(Context aContext) {
        if (mInstance == null) {
            mInstance = new DataBasePreLoad(aContext);
            dbm = DataBaseManager.getInstance(aContext);
        }
        return mInstance;
    }
    // constructor
    private DataBasePreLoad(Context aContext) {
    }

    protected void PreLoadData(SQLiteDatabase aSQLiteDatabase)
    {
        PreLoadAdminUser(aSQLiteDatabase);
        PreLoadBrewStyles(aSQLiteDatabase);
        PreLoadCalculations(aSQLiteDatabase);
    }


    private void PreLoadAdminUser(SQLiteDatabase aSQLiteDatabase)
    {
        SQLiteDatabase db = aSQLiteDatabase;

        ContentValues values = new ContentValues();
        values.put(dbm.USER_ID, 1);
        values.put(dbm.USER_NAME, "ADMIN");
        values.put(dbm.PASSWORD, "BrewTopiaAdmin123");
        values.put(dbm.CREATED_ON, APPUTILS.dateFormat.format(new Date()));

        db.insert(dbm.TABLE_USERS,null,values);

    }
    protected void PreLoadBrewStyles(SQLiteDatabase aSQLiteDatabase)
    {
        SQLiteDatabase db = aSQLiteDatabase;

        // DELETE ALL BREW STYLES ADDED BY ADMIN
        aSQLiteDatabase.execSQL("DELETE FROM "+ dbm.TABLE_BREWS_STYLES );

        String selectQuery = "SELECT * FROM " + dbm.TABLE_USERS + " WHERE "
                + dbm.USER_NAME + " = 'ADMIN'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();

            UserSchema user = new UserSchema();
            user.setUserId(c.getInt(c.getColumnIndex(dbm.USER_ID)));
            user.setUserName(c.getString(c.getColumnIndex(dbm.USER_NAME)));
            user.setPassword((c.getString(c.getColumnIndex(dbm.PASSWORD))));
            user.setCreatedOn(c.getString(c.getColumnIndex(dbm.CREATED_ON)));


            Set mapSet = (Set) APPUTILS.StyleMap.entrySet();
            //Create iterator on Set
            Iterator mapIterator = mapSet.iterator();

            while (mapIterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                // getKey Method of HashMap access a key of map
                String keyValue = (String) mapEntry.getKey();
                //getValue method returns corresponding key's value
                String value = (String) mapEntry.getValue();

                ContentValues values = new ContentValues();
                values.put(dbm.STYLE_NAME, keyValue);
                values.put(dbm.USER_ID, user.getUserId());
                values.put(dbm.STYLE_COLOR, value);

                db.insert(dbm.TABLE_BREWS_STYLES, null, values);

            }
        }
    }
    protected void PreLoadCalculations(SQLiteDatabase aSQLiteDatabase)
    {
        SQLiteDatabase db = aSQLiteDatabase;

        ContentValues values = new ContentValues();
        values.put(dbm.CALCULATION_ABV, "ABV");
        values.put(dbm.CALCULATION_NAME, "Alcohol by volume");
        db.insert(dbm.TABLE_BREWS_CALCULATIONS,null,values);

        values = new ContentValues();
        values.put(dbm.CALCULATION_ABV, "BRIX->SG");
        values.put(dbm.CALCULATION_NAME, "Brix Calculations");
        db.insert(dbm.TABLE_BREWS_CALCULATIONS,null,values);

        values = new ContentValues();
        values.put(dbm.CALCULATION_ABV, "SG->BRIX");
        values.put(dbm.CALCULATION_NAME, "Specific Gravity Calculations");
        db.insert(dbm.TABLE_BREWS_CALCULATIONS,null,values);
    }

}
