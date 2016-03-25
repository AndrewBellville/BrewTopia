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

    private static final int DATABASE_VERSION = 15;//increment to have DB changes take effect
    private static final String DATABASE_NAME = "BeerTopiaDB";

    // Log cat tag
    private static final String LOG = "DataBaseManager";

    // Table Names
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_BREWS = "Brews";
    private static final String TABLE_BREWS_STYLES = "BrewsStyles";
    private static final String TABLE_BOIL_ADDITIONS = "BoilAdditions";
    private static final String TABLE_BREWS_SCHEDULED = "BrewsScheduled";
    private static final String TABLE_BREWS_CALCULATIONS = "Calculations";

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
    private static final String STYLE = "Style";

    // TABLE_BREWS_STYLES column names
    private static final String STYLE_NAME = "StyleName";

    // BOIL_ADDITIONS column names
    private static final String ADDITION_NAME = "AdditionName";
    private static final String ADDITION_TIME = "AdditionTime";

    // BREWS_SCHEDULED column names
    private static final String SECONDARY_ALERT_DATE = "SecondaryAlertDate";
    private static final String BOTTLE_ALERT_DATE = "BottleAlertDate";
    private static final String END_BREW_DATE = "EndBrewDate";
    private static final String ACTIVE = "Active";

    // TABLE_BREWS_CALCULATIONS column names
    private static final String CALCULATION_ABV = "CalculationAbv";
    private static final String CALCULATION_NAME = "CalculationName";

    // Table Create Statements
    //CREATE_TABLE_USERS
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + USER_NAME + " TEXT PRIMARY KEY,"
            + PASSWORD + " TEXT," + CREATED_ON + " DATETIME" + ")";

    //CREATE_TABLE_BREWS
    private static final String CREATE_TABLE_BREWS = "CREATE TABLE "
            + TABLE_BREWS + "(" + BREW_NAME + " TEXT," + USER_NAME + " TEXT," + BOIL_TIME + " INTEGER," + PRIMARY + " INTEGER," + SECONDARY + " INTEGER," + BOTTLE + " INTEGER,"
            + DESCRIPTION + " TEXT," + STYLE + " TEXT," + CREATED_ON + " DATETIME, PRIMARY KEY ("+ BREW_NAME +", "+ USER_NAME +" ) )";

    //CREATE_TABLE_BREWS_STYLES
    private static final String CREATE_TABLE_BREWS_STYLES = "CREATE TABLE "
            + TABLE_BREWS_STYLES + "(" + STYLE_NAME + " TEXT," + USER_NAME + " TEXT, PRIMARY KEY ("+ STYLE_NAME +", "+ USER_NAME +" ) )";

    //CREATE_TABLE_BOIL_ADDITIONS
    private static final String CREATE_TABLE_BOIL_ADDITIONS = "CREATE TABLE "
            + TABLE_BOIL_ADDITIONS + "(" + BREW_NAME + " TEXT," + USER_NAME + " TEXT," + ADDITION_NAME + " TEXT," + ADDITION_TIME + " INTEGER, PRIMARY KEY ("+ BREW_NAME +", "+ ADDITION_NAME +", "+ USER_NAME +" ) )";

    //CREATE_TABLE_BREWS_SCHEDULED
    private static final String CREATE_TABLE_BREWS_SCHEDULED = "CREATE TABLE "
            + TABLE_BREWS_SCHEDULED + "(" + BREW_NAME + " TEXT," + USER_NAME + " TEXT," + CREATED_ON + " DATETIME," + SECONDARY_ALERT_DATE + " DATETIME," + BOTTLE_ALERT_DATE + " DATETIME,"
            + END_BREW_DATE + " DATETIME," +  ACTIVE + " INTEGER )";

    //CREATE_TABLE_BREWS_CALCULATIONS
    private static final String CREATE_TABLE_BREWS_CALCULATIONS = "CREATE TABLE "
            + TABLE_BREWS_CALCULATIONS + "(" + CALCULATION_ABV + " TEXT," + CALCULATION_NAME + " TEXT, PRIMARY KEY ("+ CALCULATION_ABV +", "+ CALCULATION_NAME +" ) )";


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
        aSQLiteDatabase.execSQL(CREATE_TABLE_BREWS_STYLES);
        aSQLiteDatabase.execSQL(CREATE_TABLE_BOIL_ADDITIONS);
        aSQLiteDatabase.execSQL(CREATE_TABLE_BREWS_SCHEDULED);
        aSQLiteDatabase.execSQL(CREATE_TABLE_BREWS_CALCULATIONS);

        //Pre Load Data
        PreLoadAdminUser(aSQLiteDatabase);
        PreLoadBrewStyles(aSQLiteDatabase);
        PreLoadCalculations(aSQLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase aSQLiteDatabase, int aOldVersion, int aNewVersion) {
        // on upgrade drop older tables
        Log.e(LOG, "Entering: onUpgrade OldVersion["+aOldVersion+"] NewVersion["+aNewVersion+"]");
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS_STYLES);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOIL_ADDITIONS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS_SCHEDULED);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS_CALCULATIONS);

        // create new tables
        onCreate(aSQLiteDatabase);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    private void PreLoadAdminUser(SQLiteDatabase aSQLiteDatabase)
    {
        SQLiteDatabase db = aSQLiteDatabase;

        ContentValues values = new ContentValues();
        values.put(USER_NAME, "ADMIN");
        values.put(PASSWORD, "");
        values.put(CREATED_ON, getDateTime());

        db.insert(TABLE_USERS,null,values);

    }
    private void PreLoadBrewStyles(SQLiteDatabase aSQLiteDatabase)
    {
        SQLiteDatabase db = aSQLiteDatabase;
        String[] brewStylesPreLoad = new String[] {"None", "Amber", "Blonde", "Brown",
                                                   "Cream", "Dark", "Fruit", "Golden",
                                                   "Honey", "India Pale Ale", "Wheat", "Red",
                                                   "Pilsner","Pale", "Light"};

        for(String brewStyle : brewStylesPreLoad)
        {
            ContentValues values = new ContentValues();
            values.put(STYLE_NAME, brewStyle);
            values.put(USER_NAME, "ADMIN");

            db.insert(TABLE_BREWS_STYLES,null,values);
        }
    }
    private void PreLoadCalculations(SQLiteDatabase aSQLiteDatabase)
    {
        SQLiteDatabase db = aSQLiteDatabase;

        ContentValues values = new ContentValues();
        values.put(CALCULATION_ABV, "ABV");
        values.put(CALCULATION_NAME, "Alcohol by volume");
        db.insert(TABLE_BREWS_CALCULATIONS,null,values);

        values = new ContentValues();
        values.put(CALCULATION_ABV, "BRIX");
        values.put(CALCULATION_NAME, "Brix Calculations");
        db.insert(TABLE_BREWS_CALCULATIONS,null,values);

        values = new ContentValues();
        values.put(CALCULATION_ABV, "SG");
        values.put(CALCULATION_NAME, "Specific Gravity");
        db.insert(TABLE_BREWS_CALCULATIONS,null,values);

    }

    //******************************User Table function*********************************

    /*
    * Creating a User
    */
    public boolean CreateAUser(UserSchema aUser) {
        SQLiteDatabase db = this.getWritableDatabase();

        if(DoesUserExist(aUser.getUserName()))
            return false;

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
    * Return true if user login exists
    */
    public boolean DoesUserLoginExist(String aUserName, String aPassword) {
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

    /*
* Return true if user exists
*/
    public boolean DoesUserExist(String aUserName) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean retVal = false;

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + USER_NAME + " = '" + aUserName + "'";

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
    public int CreateABrew(BrewSchema aBrew) {
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
        values.put(STYLE, aBrew.getStyle());
        values.put(CREATED_ON, getDateTime());

        //Add brew
        if(!(db.insert(TABLE_BREWS,null,values) > 0) )
            return 0; // 0 nothing created
        //Add Boil Additions
        if(!(add_all_boil_additions(aBrew.getBoilAdditionlist())))
            return 1;// 1 brews created

        return 2; // 2 both created
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
        brew.setStyle((c.getString(c.getColumnIndex(STYLE))));
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
                brewSchema.setStyle(c.getString(c.getColumnIndex(STYLE)));
                brewSchema.setCreatedOn(c.getString(c.getColumnIndex(CREATED_ON)));

                // adding to brewList
                brewList.add(brewSchema);
            } while (c.moveToNext());
        }

        c.close();
        return brewList;
    }

    /*
 * Updating a Brew
 */
    public Boolean updateABrew(BrewSchema aBrew) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateABrew Name["+aBrew.getBrewName()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aBrew.getBrewName());
        values.put(USER_NAME, aBrew.getUserName());
        values.put(BOIL_TIME, aBrew.getBoilTime());
        values.put(PRIMARY, aBrew.getPrimary());
        values.put(SECONDARY, aBrew.getSecondary());
        values.put(BOTTLE, aBrew.getBottle());
        values.put(DESCRIPTION, aBrew.getDescription());
        values.put(STYLE, aBrew.getStyle());
        //values.put(CREATED_ON, getDateTime());

        // updating row
        int retVal = db.update(TABLE_BREWS, values, BREW_NAME + " = ? AND " + USER_NAME + " = ?",
                new String[] { aBrew.getBrewName(), aBrew.getUserName()});

        //Update brew
        if(!(retVal > 0) )
            return false;
        //Update Boil Additions
        if(!(add_all_boil_additions(aBrew.getBoilAdditionlist())))
            return false;

        return true;
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

    //******************************Brews Style Table function*********************************
    /*
    * Creating a Brew styles
    */
    public Boolean CreateABrewStyle(BrewStyleSchema aBrewStyle) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.e(LOG, "Insert: Brew Style["+aBrewStyle.getBrewStyleName()+"]");

        ContentValues values = new ContentValues();
        values.put(STYLE_NAME, aBrewStyle.getBrewStyleName());
        values.put(USER_NAME, aBrewStyle.getUserName());

        //Add brew style
        if(!(db.insert(TABLE_BREWS_STYLES,null,values) > 0) )
            return false;

        return true;
    }

    /*
* getting all Brews styles
*/
    public List<BrewStyleSchema> getAllBrewsStyles() {
        List<BrewStyleSchema> brewStyleList = new ArrayList<BrewStyleSchema>();
        String selectQuery = "SELECT  * FROM " + TABLE_BREWS_STYLES ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllBrewsStyles Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                BrewStyleSchema brewStyleSchema = new BrewStyleSchema();
                //brewSchema.setId((c.getInt(c.getColumnIndex(ROW_ID))));
                brewStyleSchema.setBrewStyleName(c.getString(c.getColumnIndex(STYLE_NAME)));
                brewStyleSchema.setUserName(c.getString(c.getColumnIndex(USER_NAME)));

                // adding to brewStyleList
                brewStyleList.add(brewStyleSchema);
            } while (c.moveToNext());
        }

        c.close();
        return brewStyleList;
    }
    /*
* getting all Brews styles count
*/
    public int getBrewStyleCount(){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_BREWS_STYLES ;

        Cursor cursor = db.rawQuery(selectQuery, null);
        int cnt = cursor.getCount();
        cursor.close();

        return cnt;
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
* getting Boil additions for brew name and addition name
*/
    public BoilAdditionsSchema get_boil_additions_by_brew_name_addition_name(String aBrewName, String aAdditionName, String aUserName) {
        String selectQuery = "SELECT * FROM " + TABLE_BOIL_ADDITIONS + " WHERE "
                + BREW_NAME + " = '" + aBrewName+"'"
                + "AND " + USER_NAME + " = '" + aUserName+"'"
                + "AND " + ADDITION_NAME + " = '" + aAdditionName+"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        BoilAdditionsSchema baSchema = new BoilAdditionsSchema();
        baSchema.setAdditionName("");
        if (c.getCount() > 0 ) {
            c.moveToFirst();

                baSchema.setAdditionName(c.getString(c.getColumnIndex(ADDITION_NAME)));
                baSchema.setAdditionTime(c.getInt(c.getColumnIndex(ADDITION_TIME)));
        }

        c.close();
        return baSchema;
    }

    /*
* Delete All boil additions by Brew Name
*/
    public void delete_all_boil_additions_by_brew_name(String aBrewName, String aUserName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "delete_all_boil_additions_by_brew_name Name["+aBrewName+"]");

        db.delete(TABLE_BOIL_ADDITIONS, BREW_NAME + " = ? AND "+ USER_NAME +  " = ? ",
                new String[] { aBrewName, aUserName});
    }

    /*
* Delete boil additions by Brew Name and addition name
*/
    public void delete_boil_additions_by_brew_name_addition_name(String aBrewName, String aAdditionName, String aUserName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "delete_boil_additions_by_brew_name_addition_name Name["+aBrewName+"] AdditionName["+aAdditionName+"]");

        db.delete(TABLE_BOIL_ADDITIONS, BREW_NAME + " = ? AND "+ ADDITION_NAME +  " = ? AND "+ USER_NAME +  " = ? ",
                new String[] { aBrewName, aAdditionName, aUserName});
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
        values.put(SECONDARY_ALERT_DATE, aSBrew.getAlertSecondaryDate());
        values.put(BOTTLE_ALERT_DATE, aSBrew.getAlertBottleDate());
        values.put(END_BREW_DATE, aSBrew.getEndBrewDate());
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
                + "AND " + USER_NAME + " = '" + aUserName+"' "
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
                sBrew.setAlertSecondaryDate(c.getString(c.getColumnIndex(SECONDARY_ALERT_DATE)));
                sBrew.setAlertBottleDate(c.getString(c.getColumnIndex(BOTTLE_ALERT_DATE)));
                sBrew.setEndBrewDate((c.getString(c.getColumnIndex(END_BREW_DATE))));
                sBrew.setActive((c.getInt(c.getColumnIndex(ACTIVE))));

                // adding to Scheduled list if still active else set not active
                if(sBrew.getEndBrewDate().compareTo(getDateTime()) >= 0)
                    sBrewList.add(sBrew);
                else
                    setBrewScheduledNotActive(sBrew.getBrewName(), sBrew.getUserName(), sBrew.getStartDate());

            } while (c.moveToNext());
        }
        c.close();
        return sBrewList;
    }
    /*
    * Set A users Scheduled brew to not active
     */
    public void setBrewScheduledNotActive(String aBrewName, String aUserName, String aStartDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "setBrewScheduledNotActive Brew Name["+aBrewName+"] Started Date["+aStartDate+"]");

        ContentValues values = new ContentValues();
        values.put(ACTIVE, 0);

        // updating row
        db.update(TABLE_BREWS_SCHEDULED, values, BREW_NAME + " = ? AND "+ USER_NAME +  " = ? AND "+ CREATED_ON +  " = ? ",
                new String[] { aBrewName, aUserName, aStartDate});
    }

    /*
* delete A users Scheduled brew
 */
    public void deleteBrewScheduled(String aBrewName, String aUserName, String aStartDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteBrewScheduled Brew Name["+aBrewName+"] Started Date["+aStartDate+"]");

        db.delete(TABLE_BREWS_SCHEDULED, BREW_NAME + " = ? AND "+ USER_NAME +  " = ? AND "+ CREATED_ON +  " = ? ",
                new String[] { aBrewName, aUserName, aStartDate});
    }

    //************************************Calculations Table functions***************
            /*
* getting all Calculations
*/
    public List<CalculationsSchema> getAllCalculations() {
        List<CalculationsSchema> calcList = new ArrayList<CalculationsSchema>();
        String selectQuery = "SELECT * FROM " + TABLE_BREWS_CALCULATIONS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllCalculations Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                CalculationsSchema cSchema = new CalculationsSchema();
                cSchema.setCalculationAbv(c.getString(c.getColumnIndex(CALCULATION_ABV)));
                cSchema.setCalculationName(c.getString(c.getColumnIndex(CALCULATION_NAME)));

                // adding to boilList
                calcList.add(cSchema);
            } while (c.moveToNext());
        }

        c.close();
        return calcList;
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
