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
        // Started maintaining data at DB 35
        if(aOldVersion < 35)
            dropAllTables(aSQLiteDatabase);

        if(aOldVersion < 36)
        {
            //aSQLiteDatabase.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_INVENTORY_HOPS +" ADD COLUMN "+ dbm.INVENTORY_UOFM +" TEXT DEFAULT '' ");
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_INVENTORY_FERMENTABLES +" ADD COLUMN "+ dbm.INVENTORY_UOFM +" TEXT DEFAULT '' ");
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_INVENTORY_GRAINS +" ADD COLUMN "+ dbm.INVENTORY_UOFM +" TEXT DEFAULT '' ");
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_INVENTORY_YEAST +" ADD COLUMN "+ dbm.INVENTORY_UOFM +" TEXT DEFAULT '' ");
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_INVENTORY_EQUIPMENT +" ADD COLUMN "+ dbm.INVENTORY_UOFM +" TEXT DEFAULT '' ");
        }

        if(aOldVersion < 37)
        {
            aSQLiteDatabase.execSQL(dbm.CREATE_TABLE_INVENTORY_OTHER);
        }

        if(aOldVersion < 38)
        {
            aSQLiteDatabase.execSQL("DELETE FROM "+ dbm.TABLE_BREWS_CALCULATIONS +" ");
            dbm.dataBasePreLoad.PreLoadCalculations(aSQLiteDatabase);
        }
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
        if(aOldVersion < 40)
        {
            String tempUser = "CREATE TABLE "
                    + "TempUser" + "(" + dbm.USER_ID + " INTEGER PRIMARY KEY,"
                    + dbm.USER_NAME + " TEXT,"+ dbm.PASSWORD + " TEXT," + dbm.CREATED_ON + " DATETIME )";
            aSQLiteDatabase.execSQL(tempUser);

            aSQLiteDatabase.execSQL("INSERT INTO TempUser ("+ dbm.USER_ID+","+ dbm.USER_NAME+","+ dbm.PASSWORD+","+ dbm.CREATED_ON+") " +
                    "SELECT "+ dbm.ROW_ID+","+ dbm.USER_NAME+","+ dbm.PASSWORD+","+ dbm.CREATED_ON+" FROM "+ dbm.TABLE_USERS );

            aSQLiteDatabase.execSQL("DROP TABLE "+ dbm.TABLE_USERS );
            aSQLiteDatabase.execSQL("ALTER TABLE TempUser RENAME TO "+ dbm.TABLE_USERS );
        }
        if(aOldVersion < 41)
        {
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_BREWS +" ADD COLUMN "+ dbm.TOTAL_BREWED +" INTEGER DEFAULT 0 ");
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_BREWS_SCHEDULED +" ADD COLUMN "+ dbm.USING_STARTER +" INTEGER DEFAULT 0 ");
        }
        if(aOldVersion < 42)
        {
            //aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_BREWS +" ADD COLUMN "+ dbm.GLOBAL_BREW_ID +" INTEGER DEFAULT -1 ");
        }
        if(aOldVersion < 43)
        {
            //aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_BOIL_ADDITIONS +" ADD COLUMN "+ dbm.GLOBAL_ADDITION_ID +" INTEGER DEFAULT -1 ");
        }
        if(aOldVersion < 44)
        {
            //aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_BREWS_NOTES +" ADD COLUMN "+ dbm.GLOBAL_BREW_NOTE_ID +" INTEGER DEFAULT -1 ");
        }
        if(aOldVersion < 45)
        {
            aSQLiteDatabase.execSQL(dbm.CREATE_TABLE_BREW_IMAGES);
        }
        if(aOldVersion < 46)
        {
            aSQLiteDatabase.execSQL(dbm.CREATE_TABLE_SCHEDULED_EVENT);
        }
        if(aOldVersion < 47 || aOldVersion < 48)
        {
            dpl.PreLoadBrewStyles(aSQLiteDatabase);
        }
        if(aOldVersion < 49)
        {
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_BREWS +" ADD COLUMN "+ dbm.SRM +" INTEGER DEFAULT 0 ");
        }
        if(aOldVersion < 50)
        {
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_BREWS +" ADD COLUMN "+ dbm.DIRTY +" INTEGER DEFAULT 1 ");
        }
        if(aOldVersion < 51)
        {
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_BREWS +" ADD COLUMN "+ dbm.GLOBAL +" INTEGER DEFAULT 0 ");
        }
        if(aOldVersion < 52)
        {
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_BREWS +" ADD COLUMN "+ dbm.BREW_ID +" INTEGER DEFAULT -1 ");
        }
        if(aOldVersion < 53)
        {
            aSQLiteDatabase.execSQL("ALTER TABLE "+ dbm.TABLE_USERS +" ADD COLUMN "+ dbm.ROLE +" INTEGER DEFAULT 1 ");
        }
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
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_INVENTORY_GRAINS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_INVENTORY_YEAST);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_INVENTORY_EQUIPMENT);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_INVENTORY_OTHER);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_BREW_IMAGES);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbm.TABLE_SCHEDULED_EVENT);

        // create new tables
        dbm.onCreate(aSQLiteDatabase);
    }

    protected void UserSync(SQLiteDatabase aSQLiteDatabase)
    {
       /* aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_USERS +" SET "+ dbm.USER_ID +"= 2 WHERE "+dbm.USER_ID +"= 1");

        aSQLiteDatabase.execSQL("INSERT INTO "+ dbm.TABLE_USERS +" ("+ dbm.USER_ID+","+ dbm.USER_NAME+","+ dbm.PASSWORD+","+ dbm.CREATED_ON+") " +
                "SELECT 2,"+ dbm.USER_NAME+","+ dbm.PASSWORD+","+ dbm.CREATED_ON+" FROM "+ dbm.TABLE_USERS +" WHERE "+dbm.USER_ID +"= 1" );
        aSQLiteDatabase.execSQL("DELETE FROM "+ dbm.TABLE_USERS + "WHERE " +dbm.USER_ID +"= 1" );*/

        aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_BREWS +" SET "+ dbm.USER_ID +"= 3 WHERE "+dbm.USER_ID +"= 2");
        aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_BREWS_STYLES +" SET "+ dbm.USER_ID +"= 3 WHERE "+dbm.USER_ID +"= 2");
        aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_BREWS_NOTES +" SET "+ dbm.USER_ID +"= 3 WHERE "+dbm.USER_ID +"= 2");
        aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_BOIL_ADDITIONS +" SET "+ dbm.USER_ID +"= 3 WHERE "+dbm.USER_ID +"= 2");
        aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_BREWS_SCHEDULED +" SET "+ dbm.USER_ID +"= 3 WHERE "+dbm.USER_ID +"= 2");
        aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_APP_SETTINGS +" SET "+ dbm.USER_ID +"= 3 WHERE "+dbm.USER_ID +"= 2");
        aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_INVENTORY_HOPS +" SET "+ dbm.USER_ID +"= 3 WHERE "+dbm.USER_ID +"= 2");
        aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_INVENTORY_FERMENTABLES +" SET "+ dbm.USER_ID +"= 3 WHERE "+dbm.USER_ID +"= 2");
        aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_INVENTORY_GRAINS +" SET "+ dbm.USER_ID +"= 3 WHERE "+dbm.USER_ID +"= 2");
        aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_INVENTORY_YEAST +" SET "+ dbm.USER_ID +"= 3 WHERE "+dbm.USER_ID +"= 2");
        aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_INVENTORY_EQUIPMENT +" SET "+ dbm.USER_ID +"= 3 WHERE "+dbm.USER_ID +"= 2");
        aSQLiteDatabase.execSQL("UPDATE "+ dbm.TABLE_INVENTORY_OTHER +" SET "+ dbm.USER_ID +"= 3 WHERE "+dbm.USER_ID +"= 2");

    }
}
