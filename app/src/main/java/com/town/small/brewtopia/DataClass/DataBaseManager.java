package com.town.small.brewtopia.DataClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Andrew on 3/1/2015.
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;//increment to have DB changes take effect
    private static final String DATABASE_NAME = "BeerTopia";

    // Log cat tag
    private static final String LOG = "DataBaseManager";

    // Table Names
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_BREWS = "Brews";

    // Common column names across both tables
    private static final String ROW_ID = "rowid";
    private static final String CREATED_ON = "CreatedOn";
    private static final String USER_NAME = "UserName"; // primary key for  Users, Foreign key for Image, ReferenceObject

    // USERS column names
    private static final String PASSWORD = "Password";

    // BREWS column names
    private static final String BREW_NAME = "BrewName";
    private static final String PRIMARY = "PrimaryFermentation";
    private static final String SECONDARY = "Secondary";
    private static final String BOTTLE = "Bottle";
    private static final String DESCRIPTION = "Description";

    // Table Create Statements
    //CREATE_TABLE_USERS
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + USER_NAME + " TEXT PRIMARY KEY,"
            + PASSWORD + " TEXT," + CREATED_ON + " DATETIME" + ")";

    //CREATE_TABLE_BREWS
    private static final String CREATE_TABLE_BREWS = "CREATE TABLE "
            + TABLE_BREWS + "(" + BREW_NAME + " TEXT PRIMARY KEY," + PRIMARY + " INTEGER," + SECONDARY + " INTEGER," + BOTTLE + " INTEGER,"
            + DESCRIPTION + " TEXT," + CREATED_ON + " DATETIME)";


    //Singleton
    private static DataBaseManager mInstance = null;

    public static DataBaseManager getInstance(Context aContext) {
        if (mInstance == null) {
            mInstance = new DataBaseManager(aContext.getApplicationContext());
        }
        return mInstance;
    }
    // constructor
    private DataBaseManager(Context aContext) {
        super(aContext,DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase aSQLiteDatabase) {
        // creating required tables
        Log.e(LOG, "Entering: onCreate");
        aSQLiteDatabase.execSQL(CREATE_TABLE_USERS);
        aSQLiteDatabase.execSQL(CREATE_TABLE_BREWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase aSQLiteDatabase, int aOldVersion, int aNewVersion) {
        // on upgrade drop older tables
        Log.e(LOG, "Entering: onUpgrade OldVersion["+aOldVersion+"] NewVersion["+aNewVersion+"]");
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS);

        // create new tables
        onCreate(aSQLiteDatabase);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    //******************************User Table function*********************************

    /*
    * Creating a User
    */
    public boolean CreateAUser(UserSchema aUser) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, aUser.getUserName());
        values.put(PASSWORD, aUser.getPassword());
        values.put(CREATED_ON, getDateTime());

        // insert row
        Log.e(LOG, "Insert: User["+aUser.getUserName()+"] Password["+aUser.getPassword()+"]");
        return db.insert(TABLE_USERS,null,values) > 0;
    }

    /*
    * get single User
    */
    public UserSchema getUser(String aUserName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + USER_NAME + " = '" + aUserName+"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        UserSchema user = new UserSchema();
        user.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
        user.setPassword((c.getString(c.getColumnIndex(PASSWORD))));
        user.setCreatedOn(c.getString(c.getColumnIndex(CREATED_ON)));

        c.close();
        return user;
    }

    /*
    * Return true if user exists
    */
    public boolean DoesUserExist(String aUserName, String aPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean retVal = false;

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + USER_NAME + " = '" + aUserName + "' AND "
                + PASSWORD + " = '" + aPassword + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // record found return true
        if (c.getCount() > 0)
            retVal = true;

        c.close();
        return retVal;
    }

    //******************************Brews Table function*********************************
    /*
    * Creating a Brew
    */
    public boolean CreateABrew(BrewSchema aBrew) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.e(LOG, "Insert: Brew["+aBrew.getBrewName()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aBrew.getBrewName());
        values.put(PRIMARY, aBrew.getPrimary());
        values.put(SECONDARY, aBrew.getSecondary());
        values.put(BOTTLE, aBrew.getBottle());
        values.put(DESCRIPTION, aBrew.getDescription());
        values.put(CREATED_ON, getDateTime());

        // insert row
        Log.e(LOG, "Insert: Brew["+aBrew.getBrewName()+"]");
        return db.insert(TABLE_BREWS,null,values) > 0;
    }


    /*
* get single Brew
*/
    public BrewSchema getBrew(String aBrewName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_BREWS + " WHERE "
                + BREW_NAME + " = '" + aBrewName+"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        BrewSchema brew = new BrewSchema(aBrewName);
        brew.setPrimary(c.getInt(c.getColumnIndex(PRIMARY)));
        brew.setSecondary((c.getInt(c.getColumnIndex(SECONDARY))));
        brew.setBottle((c.getInt(c.getColumnIndex(BOTTLE))));
        brew.setDescription((c.getString(c.getColumnIndex(DESCRIPTION))));
        brew.setCreatedOn(c.getString(c.getColumnIndex(CREATED_ON)));

        c.close();
        return brew;
    }

    /*
* getting all Brews
*/
    public List<BrewSchema> getAllBrews() {
        List<BrewSchema> brewList = new ArrayList<BrewSchema>();
        String selectQuery = "SELECT "+ROW_ID+", * FROM " + TABLE_BREWS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllBrews Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                BrewSchema brewSchema = new BrewSchema();
                brewSchema.setId((c.getInt(c.getColumnIndex(ROW_ID))));
                brewSchema.setBrewName(c.getString(c.getColumnIndex(BREW_NAME)));
                brewSchema.setPrimary(c.getInt(c.getColumnIndex(PRIMARY)));
                brewSchema.setSecondary(c.getInt(c.getColumnIndex(SECONDARY)));
                brewSchema.setBottle(c.getInt(c.getColumnIndex(BOTTLE)));
                brewSchema.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                brewSchema.setCreatedOn(c.getString(c.getColumnIndex(CREATED_ON)));

                // adding to imageList
                brewList.add(brewSchema);
            } while (c.moveToNext());
        }

        c.close();
        return brewList;
    }

    /*
* Delete a Brew
*/
    public void DeleteBrew(String aName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "DeleteBrew Name["+aName+"]");

        db.delete(TABLE_BREWS, BREW_NAME + " = ?",
                new String[] { aName});
    }


    //************************************Helper functions***************
    /*
    * Convert Bitmap to BLOB storage byte array
     */
    private byte[] GetBitmapByteArray(Bitmap aBitmap)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        aBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        return bArray;
    }


    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
