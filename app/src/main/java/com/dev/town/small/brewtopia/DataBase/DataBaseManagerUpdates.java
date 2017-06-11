package com.dev.town.small.brewtopia.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Andrew on 7/23/2016.
 */
public class DataBaseManagerUpdates {

    // Log cat tag
    private static final String LOG = "DataBasePreLoad";

    private static DataBaseManager dbm;
    private static DataBasePreLoad dpl;

    //Singleton
    private static DataBaseManagerUpdates mInstance = null;

    public static DataBaseManagerUpdates getInstance(Context aContext) {
        if (mInstance == null) {
            mInstance = new DataBaseManagerUpdates(aContext);
            dbm = DataBaseManager.getInstance(aContext);
            dpl = DataBasePreLoad.getInstance(aContext);
        }
        return mInstance;
    }
    // constructor
    private DataBaseManagerUpdates(Context aContext) {
    }

    protected void updateAllTables(SQLiteDatabase aSQLiteDatabase, int aOldVersion)
    {
        if(aOldVersion < 2) {
            aSQLiteDatabase.execSQL("DELETE FROM " + dbm.TABLE_BREWS_CALCULATIONS + " ");
            dbm.dataBasePreLoad.PreLoadCalculations(aSQLiteDatabase);
        }
        /*
        if(aOldVersion < 39)
        {
            String tempBoilAdditions = "CREATE TABLE "
                    + "TempAdditions" + "(" + dbm.BREW_ID + " INTEGER," + dbm.USER_ID + " INTEGER," + dbm.ADDITION_NAME + " TEXT," + dbm.ADDITION_TIME + " INTEGER,"
                    +  dbm.ADDITION_QTY + " REAL," +  dbm.ADDITION_UOFM + " TEXT )";
            aSQLiteDatabase.execSQL(tempBoilAdditions);

            aSQLiteDatabase.execSQL("INSERT INTO TempAdditions ("+ dbm.BREW_ID+","+ dbm.USER_ID+","+ dbm.ADDITION_NAME+","+ dbm.ADDITION_TIME+","+ dbm.ADDITION_QTY+","+ dbm.ADDITION_UOFM+") " +
                    "SELECT "+ dbm.BREW_ID+","+ dbm.USER_ID+","+ dbm.ADDITION_NAME+","+ dbm.ADDITION_TIME+","+ dbm.ADDITION_QTY+","+ dbm.ADDITION_UOFM+" FROM "+ dbm.TABLE_BOIL_ADDITIONS );

            aSQLiteDatabase.execSQL("DROP TABLE "+ dbm.TABLE_BOIL_ADDITIONS );
            aSQLiteDatabase.execSQL("ALTER TABLE TempAdditions RENAME TO "+ dbm.TABLE_BOIL_ADDITIONS );
        }
        if(aOldVersion < 41)
        {
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_BREWS +" ADD COLUMN "+ dbm.TOTAL_BREWED +" INTEGER DEFAULT 0 ");
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_BREWS_SCHEDULED +" ADD COLUMN "+ dbm.USING_STARTER +" INTEGER DEFAULT 0 ");
        }
        */

    }

    private void dropAllTables(SQLiteDatabase aSQLiteDatabase)
    {
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_USERS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_BREWS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_BREWS_STYLES);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_BOIL_ADDITIONS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_BREWS_SCHEDULED);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_BREWS_CALCULATIONS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_BREWS_NOTES);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_APP_SETTINGS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_INVENTORY_HOPS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_INVENTORY_FERMENTABLES);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_INVENTORY_YEAST);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_INVENTORY_EQUIPMENT);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_INVENTORY_OTHER);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_BREW_IMAGES);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_SCHEDULED_EVENT);

        // create new tables
        dbm.onCreate(aSQLiteDatabase);
    }
}
