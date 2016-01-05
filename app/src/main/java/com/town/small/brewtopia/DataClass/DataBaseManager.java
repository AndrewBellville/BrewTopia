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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


/**
 * Created by Andrew on 3/1/2015.
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 8;//increment to have DB changes take effect
    private static final String DATABASE_NAME = "BeerTopiaDB";

    // Log cat tag
    private static final String LOG = "DataBaseManager";

    // Table Names
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_BREWS = "Brews";
    private static final String TABLE_BOIL_ADDITIONS = "BoilAdditions";
    private static final String TABLE_BREWS_SCHEDULED = "BrewsScheduled";

    // Common column names across both tables
    private static final String ROW_ID = "rowid";
    private static final String CREATED_ON = "CreatedOn";
    private static final String USER_NAME = "UserName"; // primary keys
    private static final String BREW_NAME = "BrewName"; // primary keys

    // USERS column names
    private static final String PASSWORD = "Password";

    // BREWS column names
    private static final String BOIL_TIME = "BoilTime";
    private static final String PRIMARY = "PrimaryFermentation";
    private static final String SECONDARY = "Secondary";
    private static final String BOTTLE = "Bottle";
    private static final String DESCRIPTION = "Description";

    // BOIL_ADDITIONS column names
    private static final String ADDITION_NAME = "AdditionName";
    private static final String ADDITION_TIME = "AdditionTime";

    // BREWS_SCHEDULED column names
    private static final String SECONDARYALERTDATE = "SecondaryAlertDate";
    private static final String BOTTLEALERTDATE = "BottleAlertDate";
    private static final String ENDBREWDATE = "EndBrewDate";
    private static final String ACTIVE = "Active";

    // Table Create Statements
    //CREATE_TABLE_USERS
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + USER_NAME + " TEXT PRIMARY KEY,"
            + PASSWORD + " TEXT," + CREATED_ON + " DATETIME" + ")";

    //CREATE_TABLE_BREWS
    private static final String CREATE_TABLE_BREWS = "CREATE TABLE "
            + TABLE_BREWS + "(" + BREW_NAME + " TEXT," + USER_NAME + " TEXT," + BOIL_TIME + " INTEGER," + PRIMARY + " INTEGER," + SECONDARY + " INTEGER," + BOTTLE + " INTEGER,"
            + DESCRIPTION + " TEXT," + CREATED_ON + " DATETIME, PRIMARY KEY ("+ BREW_NAME +", "+ USER_NAME +" ) )";

    //CREATE_TABLE_BOIL_ADDITIONS
    private static final String CREATE_TABLE_BOIL_ADDITIONS = "CREATE TABLE "
            + TABLE_BOIL_ADDITIONS + "(" + BREW_NAME + " TEXT," + USER_NAME + " TEXT," + ADDITION_NAME + " TEXT," + ADDITION_TIME + " INTEGER, PRIMARY KEY ("+ BREW_NAME +", "+ ADDITION_NAME +", "+ USER_NAME +" ) )";

    //CREATE_TABLE_BREWS_SCHEDULED
    private static final String CREATE_TABLE_BREWS_SCHEDULED = "CREATE TABLE "
            + TABLE_BREWS_SCHEDULED + "(" + BREW_NAME + " TEXT," + USER_NAME + " TEXT," + CREATED_ON + " DATETIME," + SECONDARYALERTDATE + " DATETIME," + BOTTLEALERTDATE + " DATETIME,"
            + ENDBREWDATE + " DATETIME," +  ACTIVE + " INTEGER )";


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
        aSQLiteDatabase.execSQL(CREATE_TABLE_BOIL_ADDITIONS);
        aSQLiteDatabase.execSQL(CREATE_TABLE_BREWS_SCHEDULED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase aSQLiteDatabase, int aOldVersion, int aNewVersion) {
        // on upgrade drop older tables
        Log.e(LOG, "Entering: onUpgrade OldVersion["+aOldVersion+"] NewVersion["+aNewVersion+"]");
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOIL_ADDITIONS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS_SCHEDULED);

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
        values.put(USER_NAME, aBrew.getUserName());
        values.put(BOIL_TIME, aBrew.getBoilTime());
        values.put(PRIMARY, aBrew.getPrimary());
        values.put(SECONDARY, aBrew.getSecondary());
        values.put(BOTTLE, aBrew.getBottle());
        values.put(DESCRIPTION, aBrew.getDescription());
        values.put(CREATED_ON, getDateTime());

        //Add brew
        if(!(db.insert(TABLE_BREWS,null,values) > 0) )
            return false;
        //Add Boil Additions
        if(!(add_all_boil_additions(aBrew.getBoilAdditionlist())))
            return false;

        return true;
    }


    /*
* get single Brew
*/
    public BrewSchema getBrew(String aBrewName, String aUserName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_BREWS + " WHERE "
                + BREW_NAME + " = '" + aBrewName+"'"
                + "AND " + USER_NAME + " = '" + aUserName+"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        BrewSchema brew = new BrewSchema(aBrewName);
        brew.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
        brew.setBoilTime(c.getInt(c.getColumnIndex(BOIL_TIME)));
        brew.setPrimary(c.getInt(c.getColumnIndex(PRIMARY)));
        brew.setSecondary((c.getInt(c.getColumnIndex(SECONDARY))));
        brew.setBottle((c.getInt(c.getColumnIndex(BOTTLE))));
        brew.setDescription((c.getString(c.getColumnIndex(DESCRIPTION))));
        brew.setCreatedOn(c.getString(c.getColumnIndex(CREATED_ON)));

        //set boil additions
        brew.setBoilAdditionlist(get_all_boil_additions_by_brew_name(aBrewName, aUserName));

        c.close();
        return brew;
    }

    /*
* getting all Brews for User
*/
    public List<BrewSchema> getAllBrews(String aUserName) {
        List<BrewSchema> brewList = new ArrayList<BrewSchema>();
        String selectQuery = "SELECT  * FROM " + TABLE_BREWS + " WHERE "
                + USER_NAME + " = '" + aUserName+"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllBrews Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                BrewSchema brewSchema = new BrewSchema();
                //brewSchema.setId((c.getInt(c.getColumnIndex(ROW_ID))));
                brewSchema.setBrewName(c.getString(c.getColumnIndex(BREW_NAME)));
                brewSchema.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
                brewSchema.setBoilTime(c.getInt(c.getColumnIndex(BOIL_TIME)));
                brewSchema.setPrimary(c.getInt(c.getColumnIndex(PRIMARY)));
                brewSchema.setSecondary(c.getInt(c.getColumnIndex(SECONDARY)));
                brewSchema.setBottle(c.getInt(c.getColumnIndex(BOTTLE)));
                brewSchema.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                brewSchema.setCreatedOn(c.getString(c.getColumnIndex(CREATED_ON)));

                // adding to brewList
                brewList.add(brewSchema);
            } while (c.moveToNext());
        }

        c.close();
        return brewList;
    }

    /*
* Delete a Brew
*/
    public void DeleteBrew(String aBrewName, String aUserName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "DeleteBrew User["+aUserName+"] Name["+aBrewName+"]");

        db.delete(TABLE_BREWS, BREW_NAME + " = ? AND " + USER_NAME + " = ?",
                new String[]{aBrewName, aUserName});

        //Delete all additions for this brew name
        delete_all_boil_additions_by_brew_name(aBrewName,aUserName);
    }

    //********************Boil Additions Table function*************
        /*
* add Boil additions
*/
    public boolean add_boil_additions(BoilAdditionsSchema aBoilAddition) {
        Log.e(LOG, "Insert: add_boil_additions["+aBoilAddition.getBrewName()+", "+ aBoilAddition.getAdditionName() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aBoilAddition.getBrewName());
        values.put(USER_NAME, aBoilAddition.getUserName());
        values.put(ADDITION_NAME, aBoilAddition.getAdditionName());
        values.put(ADDITION_TIME, aBoilAddition.getAdditionTime());

        // insert row
        return db.insert(TABLE_BOIL_ADDITIONS,null,values) > 0;
    }

    /*
* add Boil additions
*/
    public boolean add_all_boil_additions(List <BoilAdditionsSchema> aBoilAdditionList) {
        Log.e(LOG, "Insert: add_all_boil_additions");

        if(aBoilAdditionList.size() < 1)
            return false;

      //TODO: TRY TO FIND A WAY TO NOT DELETE BUT LOOK FOR UPDATE/ADD ONLY
      //Delete all additions for this brew name
      delete_all_boil_additions_by_brew_name(aBoilAdditionList.get(0).getBrewName(),aBoilAdditionList.get(0).getUserName());

      // for each boil addition try to add it to the DB
      for(Iterator<BoilAdditionsSchema> i = aBoilAdditionList.iterator(); i.hasNext(); )
      {
          BoilAdditionsSchema baSchema = i.next();
          if(!add_boil_additions(baSchema))
              return false;
      }

       return true;
    }

        /*
* getting all Boil additions for brew name
*/
    public List<BoilAdditionsSchema> get_all_boil_additions_by_brew_name(String aBrewName, String aUserName) {
        List<BoilAdditionsSchema> boilList = new ArrayList<BoilAdditionsSchema>();
        String selectQuery = "SELECT * FROM " + TABLE_BOIL_ADDITIONS + " WHERE "
                + BREW_NAME + " = '" + aBrewName+"'"
                + "AND " + USER_NAME + " = '" + aUserName+"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "get_all_boil_additions_by_brew_name Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                BoilAdditionsSchema baSchema = new BoilAdditionsSchema();
                baSchema.setAdditionName(c.getString(c.getColumnIndex(ADDITION_NAME)));
                baSchema.setAdditionTime(c.getInt(c.getColumnIndex(ADDITION_TIME)));

                // adding to boilList
                boilList.add(baSchema);
            } while (c.moveToNext());
        }

        c.close();
        return boilList;
    }

    /*
* Delete All boil additions by Brew Name
*/
    public void delete_all_boil_additions_by_brew_name(String aBrewName, String aUserName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "delete_all_boil_additions_by_brew_name Name["+aBrewName+"]");

        db.delete(TABLE_BOIL_ADDITIONS, BREW_NAME + " = ? AND "+ USER_NAME +  " = ?",
                new String[] { aBrewName, aUserName});
    }

//******************************Scheduled Table function*********************************
/*
* Create A Scheduled Brew
*/
    public boolean CreateAScheduledBrew(ScheduledBrewSchema aSBrew) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.e(LOG, "Insert: ScheduledBrew["+aSBrew.getUserName()+", " +aSBrew.getBrewName()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aSBrew.getBrewName());
        values.put(USER_NAME, aSBrew.getUserName());
        values.put(CREATED_ON, getDateTime());//start date
        values.put(SECONDARYALERTDATE, aSBrew.getAlertSecondaryDate());
        values.put(BOTTLEALERTDATE, aSBrew.getAlertBottleDate());
        values.put(ENDBREWDATE, aSBrew.getEndBrewDate());
        values.put(ACTIVE, aSBrew.getActive());

        //Add ScheduledBrew
        if(!(db.insert(TABLE_BREWS_SCHEDULED,null,values) > 0) )
            return false;

        return true;
    }


    /*
* Get All Active Scheduled Brews
*/
    public List<ScheduledBrewSchema> getAllActiveScheduledBrews(String aUserName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ScheduledBrewSchema> sBrewList = new ArrayList<ScheduledBrewSchema>();
        String selectQuery = "SELECT  * FROM " + TABLE_BREWS_SCHEDULED + " WHERE "
                + ACTIVE + " = 1 "
                + "AND " + USER_NAME + " = '" + aUserName+"'"
                + "ORDER BY " + CREATED_ON;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllActiveScheduledBrews Count["+c.getCount()+"]");
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                ScheduledBrewSchema sBrew = new ScheduledBrewSchema();
                sBrew.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
                sBrew.setBrewName(c.getString(c.getColumnIndex(BREW_NAME)));
                sBrew.setStartDate(c.getString(c.getColumnIndex(CREATED_ON)));
                sBrew.setAlertSecondaryDate(c.getString(c.getColumnIndex(SECONDARYALERTDATE)));
                sBrew.setAlertBottleDate(c.getString(c.getColumnIndex(BOTTLEALERTDATE)));
                sBrew.setEndBrewDate((c.getString(c.getColumnIndex(ENDBREWDATE))));
                sBrew.setActive((c.getInt(c.getColumnIndex(ACTIVE))));
                Log.e(LOG, Integer.toString(sBrew.getEndBrewDate().compareTo(getDateTime())));
                Log.e(LOG, Integer.toString(getDateTime().compareTo(sBrew.getEndBrewDate())));

                // adding to Scheduled list if still active else set not active
                if(sBrew.getEndBrewDate().compareTo(getDateTime()) >= 0)
                    sBrewList.add(sBrew);
                else
                    setBrewScheduledNotActive(sBrew.getBrewName(), sBrew.getUserName());

            } while (c.moveToNext());
        }
        c.close();
        return sBrewList;
    }
    /*
    * Set A users Scheduled brew to not active
     */
    public void setBrewScheduledNotActive(String aBrewName, String aUserName)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACTIVE, 0);

        // updating row
        db.update(TABLE_BREWS_SCHEDULED, values, BREW_NAME + " = ? AND "+ USER_NAME +  " = ?",
                new String[] { aBrewName, aUserName});

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
