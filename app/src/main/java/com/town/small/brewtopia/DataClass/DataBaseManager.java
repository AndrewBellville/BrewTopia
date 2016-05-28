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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


/**
 * Created by Andrew on 3/1/2015.
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 37;//increment to have DB changes take effect
    private static final String DATABASE_NAME = "BeerTopiaDB";

    // Log cat tag
    private static final String LOG = "DataBaseManager";

    // Table Names
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_BREWS = "Brews";
    private static final String TABLE_BREWS_STYLES = "BrewsStyles";
    private static final String TABLE_BREWS_NOTES = "BrewNotes";
    private static final String TABLE_BOIL_ADDITIONS = "BoilAdditions";
    private static final String TABLE_BREWS_SCHEDULED = "BrewsScheduled";
    private static final String TABLE_BREWS_CALCULATIONS = "Calculations";
    private static final String TABLE_APP_SETTINGS = "Settings";
    private static final String TABLE_INVENTORY_HOPS = "Hops";
    private static final String TABLE_INVENTORY_FERMENTABLES = "Fermentables";
    private static final String TABLE_INVENTORY_GRAINS = "Grains";
    private static final String TABLE_INVENTORY_YEAST = "Yeast";
    private static final String TABLE_INVENTORY_EQUIPMENT = "Equipment";
    private static final String TABLE_INVENTORY_OTHER = "Other";


    // Common column names across Mulit tables
    private static final String ROW_ID = "rowid";
    private static final String CREATED_ON = "CreatedOn";
    private static final String USER_ID = "UserId"; // primary keys
    private static final String BREW_ID = "BrewId"; // primary keys
    private static final String NOTE = "Note";
    private static final String ORIGINAL_GRAVITY = "OriginalGravity";
    private static final String FINAL_GRAVITY = "FinalGravity";
    private static final String ABV = "ABV";
    //Common column names across Inventory tables
    private static final String INVENTORY_NAME = "InventoryName";
    private static final String INVENTORY_QTY = "InventoryQty";
    private static final String INVENTORY_AMOUNT = "Amount";
    private static final String INVENTORY_UOFM = "InventoryUofM";


    // USERS column names
    private static final String USER_NAME = "UserName";
    private static final String PASSWORD = "Password";

    // BREWS column names
    private static final String BREW_NAME = "BrewName";
    private static final String BOIL_TIME = "BoilTime";
    private static final String PRIMARY = "PrimaryFermentation";
    private static final String SECONDARY = "Secondary";
    private static final String BOTTLE = "Bottle";
    private static final String DESCRIPTION = "Description";
    private static final String STYLE = "Style";
    private static final String FAVORITE = "Favorite";
    private static final String SCHEDULED = "Scheduled";
    private static final String ON_TAP = "OnTap";
    private static final String IBU = "IBU";
    private static final String METHOD = "Method";
    private static final String BATCH_SIZE = "BatchSize";
    private static final String EFFICIENCY = "Efficiency";

    // TABLE_BREWS_STYLES column names
    private static final String STYLE_NAME = "StyleName";
    private static final String STYLE_COLOR = "StyleColor";

    // BOIL_ADDITIONS column names
    private static final String ADDITION_NAME = "AdditionName";
    private static final String ADDITION_TIME = "AdditionTime";
    private static final String ADDITION_QTY = "AdditionQty";
    private static final String ADDITION_UOFM = "AdditionUofM";

    // BREWS_SCHEDULED column names
    private static final String SECONDARY_ALERT_DATE = "SecondaryAlertDate";
    private static final String BOTTLE_ALERT_DATE = "BottleAlertDate";
    private static final String END_BREW_DATE = "EndBrewDate";
    private static final String ACTIVE = "Active";
    private static final String SECONDARY_ALERT_CALENDAR_ID = "SecondaryAlertCalendarId";
    private static final String BOTTLE_ALERT_CALENDAR_ID = "BottleAlertCalendarId";
    private static final String END_BREW_CALENDAR_ID = "EndBrewCalendarId";


    // TABLE_BREWS_CALCULATIONS column names
    private static final String CALCULATION_ABV = "CalculationAbv";
    private static final String CALCULATION_NAME = "CalculationName";

    // TABLE_APP_SETTINGS column names
    private static final String SETTING_NAME = "SettingName";
    private static final String SETTING_VALUE = "SettingValue";
    private static final String SETTING_SCREEN = "SettingScreen";

    // TABLE_INVENTORY_HOPS column names
    private static final String INVENTORY_TYPE = "Type";
    private static final String INVENTORY_AA = "AlphaAcid";
    private static final String INVENTORY_USE = "Use";
    private static final String INVENTORY_TIME = "Time";
    private static final String INVENTORY_IBU = "IBU";

    // TABLE_INVENTORY_FERMENTABLES / TABLE_INVENTORY_GRAINS column names
    private static final String INVENTORY_PPG = "PoundPerGallon";
    private static final String INVENTORY_LOV = "Lovibond";
    private static final String INVENTORY_BILL = "Bill";

    // TABLE_INVENTORY_YEAST column names
    private static final String INVENTORY_ATTENUATION = "Attenuation";
    private static final String INVENTORY_FLOCCULATION = "Flocculation";
    private static final String INVENTORY_OTL = "OptimumTempLow";
    private static final String INVENTORY_OTH = "OptimumTempHigh";
    private static final String INVENTORY_STARTER = "Starter";


    // Table Create Statements
    //CREATE_TABLE_USERS
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + USER_NAME + " TEXT PRIMARY KEY,"
            + PASSWORD + " TEXT," + CREATED_ON + " DATETIME )";

    //CREATE_TABLE_BREWS
    private static final String CREATE_TABLE_BREWS = "CREATE TABLE "
            + TABLE_BREWS + "(" + BREW_NAME + " TEXT," + USER_ID + " INTEGER," + BOIL_TIME + " INTEGER," + PRIMARY + " INTEGER," + SECONDARY + " INTEGER," + BOTTLE + " INTEGER,"
            + DESCRIPTION + " TEXT," + STYLE + " TEXT," + CREATED_ON + " DATETIME," + ORIGINAL_GRAVITY + " REAL," + FINAL_GRAVITY + " REAL," + ABV + " REAL," + FAVORITE + " INTEGER,"
            + SCHEDULED + " INTEGER," + ON_TAP + " INTEGER,"+ IBU + " REAL,"+ METHOD + " TEXT,"+ BATCH_SIZE + " REAL,"+ EFFICIENCY + " REAL, PRIMARY KEY ("+ BREW_NAME +", "+ USER_ID +" ) )";

    //CREATE_TABLE_BREWS_STYLES
    private static final String CREATE_TABLE_BREWS_STYLES = "CREATE TABLE "
            + TABLE_BREWS_STYLES + "(" + STYLE_NAME + " TEXT," + USER_ID + " INTEGER," + STYLE_COLOR + " TEXT, PRIMARY KEY ("+ STYLE_NAME +", "+ USER_ID +" ) )";

    //CREATE_TABLE_BOIL_ADDITIONS
    private static final String CREATE_TABLE_BOIL_ADDITIONS = "CREATE TABLE "
            + TABLE_BOIL_ADDITIONS + "(" + BREW_ID + " INTEGER," + USER_ID + " INTEGER," + ADDITION_NAME + " TEXT," + ADDITION_TIME + " INTEGER,"
            +  ADDITION_QTY + " REAL," +  ADDITION_UOFM + " TEXT, PRIMARY KEY ("+ BREW_ID +", "+ ADDITION_NAME +", "+ USER_ID +" ) )";

    //CREATE_TABLE_BREWS_SCHEDULED
    private static final String CREATE_TABLE_BREWS_SCHEDULED = "CREATE TABLE "
            + TABLE_BREWS_SCHEDULED + "(" + BREW_ID + " INTEGER," + USER_ID + " INTEGER," + BREW_NAME + " TEXT," + CREATED_ON + " DATETIME," + SECONDARY_ALERT_DATE
            + " DATETIME," + BOTTLE_ALERT_DATE + " DATETIME," + END_BREW_DATE + " DATETIME," +  ACTIVE + " INTEGER," +  NOTE + " TEXT," +  STYLE_COLOR + " TEXT,"
            + ORIGINAL_GRAVITY + " REAL," + FINAL_GRAVITY + " REAL," + ABV + " REAL," + SECONDARY_ALERT_CALENDAR_ID + " INTEGER,"+ BOTTLE_ALERT_CALENDAR_ID
            + " INTEGER,"+ END_BREW_CALENDAR_ID + " INTEGER )";

    //CREATE_TABLE_BREWS_CALCULATIONS
    private static final String CREATE_TABLE_BREWS_CALCULATIONS = "CREATE TABLE "
            + TABLE_BREWS_CALCULATIONS + "(" + CALCULATION_ABV + " TEXT," + CALCULATION_NAME + " TEXT, PRIMARY KEY ("+ CALCULATION_ABV +", "+ CALCULATION_NAME +" ) )";

    //CREATE_TABLE_BREWS_NOTES
    private static final String CREATE_TABLE_BREWS_NOTES = "CREATE TABLE "
            + TABLE_BREWS_NOTES + "(" + BREW_ID + " INTEGER," + USER_ID + " INTEGER," + NOTE + " TEXT," + CREATED_ON + " DATETIME )";

    //CREATE_TABLE_APP_SETTINGS
    private static final String CREATE_TABLE_APP_SETTINGS = "CREATE TABLE "
            + TABLE_APP_SETTINGS + "(" + USER_ID + " INTEGER," + SETTING_NAME + " TEXT," + SETTING_VALUE + " TEXT," + SETTING_SCREEN + " TEXT )";

    //CREATE_TABLE_INVENTORY_HOPS
    private static final String CREATE_TABLE_INVENTORY_HOPS = "CREATE TABLE "
            + TABLE_INVENTORY_HOPS + "(" + USER_ID + " INTEGER," + BREW_ID + " INTEGER," + INVENTORY_QTY + " INTEGER," + INVENTORY_NAME + " TEXT,"
            + INVENTORY_AMOUNT + " REAL," + INVENTORY_TYPE + " TEXT," + INVENTORY_AA + " REAL," + INVENTORY_USE + " TEXT," + INVENTORY_TIME + " INTEGER," + INVENTORY_UOFM + " TEXT,"
            + INVENTORY_IBU + " REAL )";

    //CREATE_TABLE_INVENTORY_FERMENTABLES
    private static final String CREATE_TABLE_INVENTORY_FERMENTABLES = "CREATE TABLE "
            + TABLE_INVENTORY_FERMENTABLES + "(" + USER_ID + " INTEGER," + BREW_ID + " INTEGER," + INVENTORY_QTY + " INTEGER," + INVENTORY_NAME + " TEXT,"
            + INVENTORY_AMOUNT + " REAL," + INVENTORY_PPG + " REAL," + INVENTORY_LOV + " REAL," + INVENTORY_UOFM + " TEXT," + INVENTORY_BILL + " REAL )";

    //CREATE_TABLE_INVENTORY_GRAINS
    private static final String CREATE_TABLE_INVENTORY_GRAINS = "CREATE TABLE "
            + TABLE_INVENTORY_GRAINS + "(" + USER_ID + " INTEGER," + BREW_ID + " INTEGER," + INVENTORY_QTY + " INTEGER," + INVENTORY_NAME + " TEXT,"
            + INVENTORY_AMOUNT + " REAL," + INVENTORY_PPG + " REAL," + INVENTORY_LOV + " REAL," + INVENTORY_UOFM + " TEXT," + INVENTORY_BILL + " REAL )";

    //CREATE_TABLE_INVENTORY_YEAST
    private static final String CREATE_TABLE_INVENTORY_YEAST = "CREATE TABLE "
            + TABLE_INVENTORY_YEAST + "(" + USER_ID + " INTEGER," + BREW_ID + " INTEGER," + INVENTORY_QTY + " INTEGER," + INVENTORY_NAME + " TEXT,"
            + INVENTORY_AMOUNT + " REAL," + INVENTORY_FLOCCULATION + " TEXT," + INVENTORY_STARTER + " INTEGER," + INVENTORY_ATTENUATION + " REAL,"
            + INVENTORY_OTL + " REAL," + INVENTORY_UOFM + " TEXT," + INVENTORY_OTH + " REAL )";

    //CREATE_TABLE_INVENTORY_EQUIPMENT
    private static final String CREATE_TABLE_INVENTORY_EQUIPMENT = "CREATE TABLE "
            + TABLE_INVENTORY_EQUIPMENT + "(" + USER_ID + " INTEGER," + BREW_ID + " INTEGER," + INVENTORY_QTY + " INTEGER," + INVENTORY_NAME + " TEXT,"
            + INVENTORY_UOFM + " TEXT," + INVENTORY_AMOUNT + " REAL )";

    //CREATE_TABLE_INVENTORY_OTHER
    private static final String CREATE_TABLE_INVENTORY_OTHER = "CREATE TABLE "
            + TABLE_INVENTORY_OTHER + "(" + USER_ID + " INTEGER," + BREW_ID + " INTEGER," + INVENTORY_QTY + " INTEGER," + INVENTORY_NAME + " TEXT,"
            + INVENTORY_UOFM + " TEXT," + INVENTORY_AMOUNT + " REAL )";


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
        aSQLiteDatabase.execSQL(CREATE_TABLE_BREWS_NOTES);
        aSQLiteDatabase.execSQL(CREATE_TABLE_APP_SETTINGS);
        aSQLiteDatabase.execSQL(CREATE_TABLE_INVENTORY_HOPS);
        aSQLiteDatabase.execSQL(CREATE_TABLE_INVENTORY_FERMENTABLES);
        aSQLiteDatabase.execSQL(CREATE_TABLE_INVENTORY_GRAINS);
        aSQLiteDatabase.execSQL(CREATE_TABLE_INVENTORY_YEAST);
        aSQLiteDatabase.execSQL(CREATE_TABLE_INVENTORY_EQUIPMENT);
        aSQLiteDatabase.execSQL(CREATE_TABLE_INVENTORY_OTHER);

        //Pre Load Data
        PreLoadAdminUser(aSQLiteDatabase);
        PreLoadBrewStyles(aSQLiteDatabase);
        PreLoadCalculations(aSQLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase aSQLiteDatabase, int aOldVersion, int aNewVersion) {
        // on upgrade drop older tables
        Log.e(LOG, "Entering: onUpgrade OldVersion["+aOldVersion+"] NewVersion["+aNewVersion+"]");
        updateAllTables(aSQLiteDatabase, aOldVersion);
    }

    private void updateAllTables(SQLiteDatabase aSQLiteDatabase, int aOldVersion)
    {
        // Started maintaining data at DB 35
        if(aOldVersion < 35)
            dropAllTables(aSQLiteDatabase);

        if(aOldVersion < 36)
        {
            //aSQLiteDatabase.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
            aSQLiteDatabase.execSQL("ALTER TABLE "+ TABLE_INVENTORY_HOPS +" ADD COLUMN "+ INVENTORY_UOFM +" TEXT DEFAULT '' ");
            aSQLiteDatabase.execSQL("ALTER TABLE "+ TABLE_INVENTORY_FERMENTABLES +" ADD COLUMN "+ INVENTORY_UOFM +" TEXT DEFAULT '' ");
            aSQLiteDatabase.execSQL("ALTER TABLE "+ TABLE_INVENTORY_GRAINS +" ADD COLUMN "+ INVENTORY_UOFM +" TEXT DEFAULT '' ");
            aSQLiteDatabase.execSQL("ALTER TABLE "+ TABLE_INVENTORY_YEAST +" ADD COLUMN "+ INVENTORY_UOFM +" TEXT DEFAULT '' ");
            aSQLiteDatabase.execSQL("ALTER TABLE "+ TABLE_INVENTORY_EQUIPMENT +" ADD COLUMN "+ INVENTORY_UOFM +" TEXT DEFAULT '' ");
        }

        if(aOldVersion < 37)
        {
            aSQLiteDatabase.execSQL(CREATE_TABLE_INVENTORY_OTHER);
        }
    }

    private void dropAllTables(SQLiteDatabase aSQLiteDatabase)
    {
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS_STYLES);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOIL_ADDITIONS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS_SCHEDULED);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS_CALCULATIONS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BREWS_NOTES);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_SETTINGS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY_HOPS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY_FERMENTABLES);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY_GRAINS);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY_YEAST);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY_EQUIPMENT);
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY_OTHER);

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

        String selectQuery = "SELECT "+ROW_ID+", * FROM " + TABLE_USERS + " WHERE "
                + USER_NAME + " = 'ADMIN'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();

            UserSchema user = new UserSchema();
            user.setUserId(c.getInt(c.getColumnIndex(ROW_ID)));
            user.setUserName(c.getString(c.getColumnIndex(USER_NAME)));
            user.setPassword((c.getString(c.getColumnIndex(PASSWORD))));
            user.setCreatedOn(c.getString(c.getColumnIndex(CREATED_ON)));


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
                values.put(STYLE_NAME, keyValue);
                values.put(USER_ID, user.getUserId());
                values.put(STYLE_COLOR, value);

                db.insert(TABLE_BREWS_STYLES, null, values);

            }
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

        String selectQuery = "SELECT "+ROW_ID+", * FROM " + TABLE_USERS + " WHERE "
                + USER_NAME + " = '" + aUserName+"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        UserSchema user = new UserSchema();
        user.setUserId(c.getLong(c.getColumnIndex(ROW_ID)));
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
    public long CreateABrew(BrewSchema aBrew) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.e(LOG, "Insert: Brew["+aBrew.getBrewName()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_NAME, aBrew.getBrewName());
        values.put(USER_ID, aBrew.getUserId());
        values.put(BOIL_TIME, aBrew.getBoilTime());
        values.put(PRIMARY, aBrew.getPrimary());
        values.put(SECONDARY, aBrew.getSecondary());
        values.put(BOTTLE, aBrew.getBottle());
        values.put(ORIGINAL_GRAVITY, aBrew.getTargetOG());
        values.put(FINAL_GRAVITY, aBrew.getTargetFG());
        values.put(ABV, aBrew.getTargetABV());
        values.put(DESCRIPTION, aBrew.getDescription());
        values.put(STYLE, aBrew.getStyle());
        values.put(FAVORITE, aBrew.getFavorite());
        values.put(SCHEDULED, aBrew.getScheduled());
        values.put(ON_TAP, aBrew.getOnTap());
        values.put(IBU, aBrew.getIBU());
        values.put(METHOD, aBrew.getMethod());
        values.put(CREATED_ON, getDateTime());
        values.put(BATCH_SIZE, aBrew.getBatchSize());
        values.put(EFFICIENCY, aBrew.getEfficiency());

        //Add brew
        long row_id = db.insert(TABLE_BREWS,null,values);
        if(!(row_id > 0) )
            return 0; // 0 nothing created
        else
        {
            for(BoilAdditionsSchema boilAdditionsSchema: aBrew.getBoilAdditionlist())
            {
                boilAdditionsSchema.setBrewId(row_id);
            }
            for(BrewNoteSchema brewNoteSchema: aBrew.getBrewNoteSchemaList())
            {
                brewNoteSchema.setBrewId(row_id);
            }
            for(InventorySchema inventorySchema: aBrew.getBrewInventorySchemaList())
            {
                inventorySchema.setBrewId(row_id);
            }
        }
        if(!(add_all_boil_additions(aBrew.getBoilAdditionlist())))
            return -1;// -1 failed created
        if(!(addAllBrewNotes(aBrew.getBrewNoteSchemaList())))
            return -1;// -1 failed created
        if(!(CreateBrewInventoryHelper(aBrew.getBrewInventorySchemaList())))
            return -1;// -1 failed created

        return row_id; // All create created retrun brew Id
    }


    /*
* get single Brew
*/
    public BrewSchema getBrew(long aBrewId, long aUserId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT "+ ROW_ID+",* FROM " + TABLE_BREWS + " WHERE "
                + ROW_ID + " = " + aBrewId+" "
                + "AND " + USER_ID + " = " + aUserId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        BrewSchema brew = new BrewSchema();
        if (c != null)
        {
            c.moveToFirst();

            brew.setBrewId(c.getLong(c.getColumnIndex(ROW_ID)));
            brew.setBrewName((c.getString(c.getColumnIndex(BREW_NAME))));
            brew.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
            brew.setBoilTime(c.getInt(c.getColumnIndex(BOIL_TIME)));
            brew.setPrimary(c.getInt(c.getColumnIndex(PRIMARY)));
            brew.setSecondary((c.getInt(c.getColumnIndex(SECONDARY))));
            brew.setBottle((c.getInt(c.getColumnIndex(BOTTLE))));
            brew.setTargetOG(c.getDouble(c.getColumnIndex(ORIGINAL_GRAVITY)));
            brew.setTargetFG((c.getDouble(c.getColumnIndex(FINAL_GRAVITY))));
            brew.setTargetABV((c.getDouble(c.getColumnIndex(ABV))));
            brew.setDescription((c.getString(c.getColumnIndex(DESCRIPTION))));
            brew.setStyle((c.getString(c.getColumnIndex(STYLE))));
            brew.setFavorite((c.getInt(c.getColumnIndex(FAVORITE))));
            brew.setScheduled((c.getInt(c.getColumnIndex(SCHEDULED))));
            brew.setOnTap((c.getInt(c.getColumnIndex(ON_TAP))));
            brew.setCreatedOn(c.getString(c.getColumnIndex(CREATED_ON)));
            brew.setIBU((c.getDouble(c.getColumnIndex(IBU))));
            brew.setMethod(c.getString(c.getColumnIndex(METHOD)));
            brew.setBatchSize((c.getDouble(c.getColumnIndex(BATCH_SIZE))));
            brew.setEfficiency((c.getDouble(c.getColumnIndex(EFFICIENCY))));

            //set boil additions
            brew.setBoilAdditionlist(get_all_boil_additions_by_brew_name(aBrewId, aUserId));

            //set style
            brew.setStyleSchema(getBrewsStylesByName(brew.getStyle()));

            //set notes
            brew.setBrewNoteSchemaList(getAllBrewNotes(aBrewId, aUserId));
        }


        c.close();
        return brew;
    }

    /*
* getting all Brews for User
*/
    public List<BrewSchema> getAllBrews(long aUserId) {
        List<BrewSchema> brewList = new ArrayList<BrewSchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_BREWS + " WHERE "
                + USER_ID + " = " + aUserId+" "
                + "ORDER BY " + FAVORITE+" DESC,"+SCHEDULED+" DESC,"+ON_TAP+" DESC,"+BREW_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllBrews Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                BrewSchema brewSchema = new BrewSchema();
                brewSchema.setBrewId((c.getLong(c.getColumnIndex(ROW_ID))));
                brewSchema.setUserId(c.getLong(c.getColumnIndex(USER_ID)));

                brewSchema = getBrew(brewSchema.getBrewId(),brewSchema.getUserId());

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
        values.put(USER_ID, aBrew.getUserId());
        values.put(BOIL_TIME, aBrew.getBoilTime());
        values.put(PRIMARY, aBrew.getPrimary());
        values.put(SECONDARY, aBrew.getSecondary());
        values.put(BOTTLE, aBrew.getBottle());
        values.put(ORIGINAL_GRAVITY, aBrew.getTargetOG());
        values.put(FINAL_GRAVITY, aBrew.getTargetFG());
        values.put(ABV, aBrew.getTargetABV());
        values.put(DESCRIPTION, aBrew.getDescription());
        values.put(STYLE, aBrew.getStyle());
        values.put(FAVORITE, aBrew.getFavorite());
        values.put(SCHEDULED, aBrew.getScheduled());
        values.put(ON_TAP, aBrew.getOnTap());
        values.put(IBU, aBrew.getIBU());
        values.put(METHOD, aBrew.getMethod());
        values.put(BATCH_SIZE, aBrew.getBatchSize());
        values.put(EFFICIENCY, aBrew.getEfficiency());
        //values.put(CREATED_ON, getDateTime());

        // updating row
        int retVal = db.update(TABLE_BREWS, values, ROW_ID + " = ?",
                new String[] { Long.toString(aBrew.getBrewId()) });

        //Update brew
        if(!(retVal > 0) )
            return false;
        //Update Boil Additions
        if(!(add_all_boil_additions(aBrew.getBoilAdditionlist())))
            return false;
        //Update Brew notes
        if(!(addAllBrewNotes(aBrew.getBrewNoteSchemaList())))
            return false;

        return true;
    }

    /*
* Delete a Brew
*/
    public void DeleteBrew(long aBrewId, long aUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "DeleteBrew User["+aUserId+"] Name["+aBrewId+"]");

        db.delete(TABLE_BREWS, ROW_ID + " = ? AND " + USER_ID + " = ?",
                new String[]{Long.toString(aBrewId), Long.toString(aUserId)});

        //Delete all additions for this brew name
        delete_all_boil_additions_by_brew_name(aBrewId,aUserId);

        //Delete all brew notes
        deleteAllBrewNotes(aBrewId,aUserId);

        //delete all schedules
        deleteBrewScheduled(aBrewId,aUserId);

        //delete all Inventory
        deleteHopsByBrewIdUserId(aBrewId,aUserId);
        deleteFermentablesByBrewIdUserId(aBrewId,aUserId);
        deleteGrainsByBrewIdUserId(aBrewId,aUserId);
        deleteYeastByBrewIdUserId(aBrewId,aUserId);
        deleteEquipmentByBrewIdUserId(aBrewId,aUserId);
        deleteOtherByBrewIdUserId(aBrewId,aUserId);
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
        values.put(USER_ID, aBrewStyle.getUserId());
        values.put(STYLE_COLOR, aBrewStyle.getBrewStyleColor());

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
                brewStyleSchema.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
                brewStyleSchema.setBrewStyleColor(c.getString(c.getColumnIndex(STYLE_COLOR)));

                // adding to brewStyleList
                brewStyleList.add(brewStyleSchema);
            } while (c.moveToNext());
        }

        c.close();
        return brewStyleList;
    }

    /*
* getting a Brews styles
*/
    public BrewStyleSchema getBrewsStylesByName(String aStyleName) {
        String selectQuery = "SELECT  * FROM " + TABLE_BREWS_STYLES + " WHERE "
                + STYLE_NAME + " = '" + aStyleName+"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllBrewsStyles Count["+c.getCount()+"]");
        BrewStyleSchema brewStyleSchema = new BrewStyleSchema();
        if (c.getCount() > 0 ) {
            c.moveToFirst();
                //brewSchema.setId((c.getInt(c.getColumnIndex(ROW_ID))));
                brewStyleSchema.setBrewStyleName(c.getString(c.getColumnIndex(STYLE_NAME)));
                brewStyleSchema.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
                brewStyleSchema.setBrewStyleColor(c.getString(c.getColumnIndex(STYLE_COLOR)));
        }

        c.close();
        return brewStyleSchema;
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
    //************************************Brew Notes Table functions***************
        /*
* add Brew Note
*/
    public boolean addBrewNote(BrewNoteSchema aBrewNote) {
        Log.e(LOG, "Insert: addBrewNote["+aBrewNote.getBrewId()+", "+ aBrewNote.getUserId() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BREW_ID, aBrewNote.getBrewId());
        values.put(USER_ID, aBrewNote.getUserId());
        values.put(CREATED_ON, getDateTime());
        values.put(NOTE, aBrewNote.getBrewNote());


        // insert row
        return db.insert(TABLE_BREWS_NOTES,null,values) > 0;
    }

    /*
* add All brew notes
*/
    public boolean addAllBrewNotes(List <BrewNoteSchema> aBrewNoteList) {
        Log.e(LOG, "Insert: addAllBrewNotes");

        if(aBrewNoteList.size() < 1)
            return true;

        //TODO: TRY TO FIND A WAY TO NOT DELETE BUT LOOK FOR UPDATE/ADD ONLY
        //Delete all additions for this brew name
        deleteAllBrewNotes(aBrewNoteList.get(0).getBrewId(),aBrewNoteList.get(0).getUserId());

        // for each brew note try to add it to the DB
        for(Iterator<BrewNoteSchema> i = aBrewNoteList.iterator(); i.hasNext(); )
        {
            BrewNoteSchema brewNoteSchema = i.next();
            if(!addBrewNote(brewNoteSchema))
                return false;
        }

        return true;
    }

    /*
* getting All brew notes by brew name and user id
*/
    public List<BrewNoteSchema> getAllBrewNotes(long aBrewId, long aUserId) {
        List<BrewNoteSchema> brewNoteSchemaList = new ArrayList<BrewNoteSchema>();
        String selectQuery = "SELECT "+ ROW_ID +",* FROM " + TABLE_BREWS_NOTES + " WHERE "
                + BREW_ID + " = " + aBrewId+" "
                + "AND " + USER_ID + " = " + aUserId;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllBrewNotes Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                BrewNoteSchema brewNoteSchema = new BrewNoteSchema();
                brewNoteSchema.setNoteId(c.getLong(c.getColumnIndex(ROW_ID)));
                brewNoteSchema.setBrewId(c.getLong(c.getColumnIndex(BREW_ID)));
                brewNoteSchema.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
                brewNoteSchema.setBrewNote(c.getString(c.getColumnIndex(NOTE)));
                brewNoteSchema.setCreatedOn(c.getString(c.getColumnIndex(CREATED_ON)));

                // adding to boilList
                brewNoteSchemaList.add(brewNoteSchema);
            } while (c.moveToNext());
        }

        c.close();
        return brewNoteSchemaList;
    }

    /*
* Update Brew notes
*/
    public boolean updateBrewNotes(BrewNoteSchema aBrewNoteSchema) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateBrewNotes Name["+aBrewNoteSchema.getBrewId()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_ID, aBrewNoteSchema.getBrewId());
        values.put(USER_ID, aBrewNoteSchema.getUserId());
        values.put(CREATED_ON, getDateTime());
        values.put(NOTE, aBrewNoteSchema.getBrewNote());

        // updating row
        int retVal = db.update(TABLE_BREWS_NOTES, values, ROW_ID + " = ?",
                new String[] { Long.toString(aBrewNoteSchema.getNoteId()) });

        if(!(retVal > 0) )
            return false;

        return true;
    }

    /*
* Delete All brew notes by Brew Name
*/
    public void deleteAllBrewNotes( long aBrewId, long aUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteAllBrewNotes Name["+aBrewId+"]");

        db.delete(TABLE_BREWS_NOTES, BREW_ID + " = ? AND "+ USER_ID +  " = ? ",
                new String[] { Long.toString(aBrewId), Long.toString(aUserId)});
    }

    /*
* delete brew note by id
*/
    public void deleteBrewNoteById(long brewNoteId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteBrewNoteById Note Id["+Long.toString(brewNoteId) +"]");

        db.delete(TABLE_BREWS_NOTES, ROW_ID + " = ?",
                new String[] { Long.toString(brewNoteId) });
    }


    //********************Boil Additions Table function*************
        /*
* add Boil additions
*/
    public boolean add_boil_additions(BoilAdditionsSchema aBoilAddition) {
        Log.e(LOG, "Insert: add_boil_additions["+aBoilAddition.getBrewId()+", "+ aBoilAddition.getAdditionName() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BREW_ID, aBoilAddition.getBrewId());
        values.put(USER_ID, aBoilAddition.getUserId());
        values.put(ADDITION_NAME, aBoilAddition.getAdditionName());
        values.put(ADDITION_TIME, aBoilAddition.getAdditionTime());
        values.put(ADDITION_QTY, aBoilAddition.getAdditionQty());
        values.put(ADDITION_UOFM, aBoilAddition.getUOfM());

        // insert row
        return db.insert(TABLE_BOIL_ADDITIONS,null,values) > 0;
    }

    /*
* add Boil additions
*/
    public boolean add_all_boil_additions(List <BoilAdditionsSchema> aBoilAdditionList) {
        Log.e(LOG, "Insert: add_all_boil_additions");

        if(aBoilAdditionList.size() < 1)
            return  true;

      //TODO: TRY TO FIND A WAY TO NOT DELETE BUT LOOK FOR UPDATE/ADD ONLY
      //Delete all additions for this brew name
      delete_all_boil_additions_by_brew_name(aBoilAdditionList.get(0).getBrewId(),aBoilAdditionList.get(0).getUserId());

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
* Update Boil additions
*/
    public boolean update_boil_addition(BoilAdditionsSchema aBoilAddition) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "update_boil_addition Name["+aBoilAddition.getBrewId()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_ID, aBoilAddition.getBrewId());
        values.put(USER_ID, aBoilAddition.getUserId());
        values.put(ADDITION_NAME, aBoilAddition.getAdditionName());
        values.put(ADDITION_TIME, aBoilAddition.getAdditionTime());
        values.put(ADDITION_QTY, aBoilAddition.getAdditionQty());
        values.put(ADDITION_UOFM, aBoilAddition.getUOfM());

        // updating row
        int retVal = db.update(TABLE_BOIL_ADDITIONS, values, ROW_ID + " = ?",
                new String[] { Long.toString(aBoilAddition.getAdditionId()) });

        if(!(retVal > 0) )
            return false;

        return true;
    }

        /*
* getting all Boil additions for brew name
*/
    public List<BoilAdditionsSchema> get_all_boil_additions_by_brew_name(long aBrewId, long aUserId) {
        List<BoilAdditionsSchema> boilList = new ArrayList<BoilAdditionsSchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_BOIL_ADDITIONS + " WHERE "
                + BREW_ID + " = " + aBrewId+" "
                + "AND " + USER_ID + " = " + aUserId;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "get_all_boil_additions_by_brew_name Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                BoilAdditionsSchema baSchema = new BoilAdditionsSchema();
                baSchema.setAdditionId(c.getLong(c.getColumnIndex(ROW_ID)));
                baSchema.setBrewId(c.getLong(c.getColumnIndex(BREW_ID)));
                baSchema.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
                baSchema.setAdditionName(c.getString(c.getColumnIndex(ADDITION_NAME)));
                baSchema.setAdditionTime(c.getInt(c.getColumnIndex(ADDITION_TIME)));
                baSchema.setAdditionQty(c.getDouble(c.getColumnIndex(ADDITION_QTY)));
                baSchema.setUOfM(c.getString(c.getColumnIndex(ADDITION_UOFM)));

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
    public void delete_all_boil_additions_by_brew_name(long aBrewId, long aUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "delete_all_boil_additions_by_brew_name Name["+aBrewId+"]");

        db.delete(TABLE_BOIL_ADDITIONS, BREW_ID + " = ? AND "+ USER_ID +  " = ? ",
                new String[] { Long.toString(aBrewId), Long.toString(aUserId) });
    }

    /*
* delete boil additions by id
*/
    public void delete_all_boil_additions_by_id(long boilAdditionId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteBrewNoteById Note Id["+Long.toString(boilAdditionId) +"]");

        db.delete(TABLE_BOIL_ADDITIONS, ROW_ID + " = ?",
                new String[] { Long.toString(boilAdditionId) });
    }

//******************************Scheduled Table function*********************************
/*
* Create A Scheduled Brew
*/
    public boolean CreateAScheduledBrew(ScheduledBrewSchema aSBrew) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.e(LOG, "Insert: ScheduledBrew["+aSBrew.getUserId()+", " +aSBrew.getBrewId()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_ID, aSBrew.getBrewId());
        values.put(USER_ID, aSBrew.getUserId());
        values.put(BREW_NAME, aSBrew.getBrewName());
        values.put(CREATED_ON, getDateTime());//start date
        values.put(SECONDARY_ALERT_DATE, aSBrew.getAlertSecondaryDate());
        values.put(BOTTLE_ALERT_DATE, aSBrew.getAlertBottleDate());
        values.put(END_BREW_DATE, aSBrew.getEndBrewDate());
        values.put(ACTIVE, aSBrew.getActive());
        values.put(NOTE, "");
        values.put(STYLE_COLOR, aSBrew.getColor());
        values.put(ORIGINAL_GRAVITY, aSBrew.getOG());
        values.put(FINAL_GRAVITY, aSBrew.getFG());
        values.put(ABV, aSBrew.getABV());
        values.put(SECONDARY_ALERT_CALENDAR_ID, aSBrew.getAlertSecondaryCalendarId());
        values.put(BOTTLE_ALERT_CALENDAR_ID, aSBrew.getAlertBottleCalendarId());
        values.put(END_BREW_CALENDAR_ID, aSBrew.getEndBrewCalendarId());

        //Add ScheduledBrew
        if(!(db.insert(TABLE_BREWS_SCHEDULED,null,values) > 0) )
            return false;

        return true;
    }
    /*
* Get Active Scheduled Brew by name and date
*/
    public ScheduledBrewSchema getActiveScheduledBrewByNameDate(long aBrewId, long aUserId, String aStartDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_BREWS_SCHEDULED + " WHERE "
                + ACTIVE + " = 1 "
                + "AND " + BREW_ID + " = " + aBrewId +" "
                + "AND " + USER_ID + " = " + aUserId +" "
                + "AND " + CREATED_ON + " = '" + aStartDate +"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        ScheduledBrewSchema sBrew = new ScheduledBrewSchema();
        if (c != null) {
            c.moveToFirst();
            sBrew.setScheduleId(c.getLong(c.getColumnIndex(ROW_ID)));
            sBrew.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
            sBrew.setBrewId(c.getLong(c.getColumnIndex(BREW_ID)));
            sBrew.setBrewName(c.getString(c.getColumnIndex(BREW_NAME)));
            sBrew.setStartDate(c.getString(c.getColumnIndex(CREATED_ON)));
            sBrew.setAlertSecondaryDate(c.getString(c.getColumnIndex(SECONDARY_ALERT_DATE)));
            sBrew.setAlertBottleDate(c.getString(c.getColumnIndex(BOTTLE_ALERT_DATE)));
            sBrew.setEndBrewDate((c.getString(c.getColumnIndex(END_BREW_DATE))));
            sBrew.setOG(c.getDouble(c.getColumnIndex(ORIGINAL_GRAVITY)));
            sBrew.setFG((c.getDouble(c.getColumnIndex(FINAL_GRAVITY))));
            sBrew.setABV((c.getDouble(c.getColumnIndex(ABV))));
            sBrew.setActive((c.getInt(c.getColumnIndex(ACTIVE))));
            sBrew.setNotes((c.getString(c.getColumnIndex(NOTE))));
            sBrew.setColor((c.getString(c.getColumnIndex(STYLE_COLOR))));
            sBrew.setAlertSecondaryCalendarId((c.getLong(c.getColumnIndex(SECONDARY_ALERT_CALENDAR_ID))));
            sBrew.setAlertBottleCalendarId((c.getLong(c.getColumnIndex(BOTTLE_ALERT_CALENDAR_ID))));
            sBrew.setEndBrewCalendarId((c.getLong(c.getColumnIndex(END_BREW_CALENDAR_ID))));
        }
        c.close();
        return sBrew;
    }

    /*
* Get Active Scheduled Brew by Id
*/
    public ScheduledBrewSchema getActiveScheduledBrewId(long ScheduleId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_BREWS_SCHEDULED + " WHERE "
                + ACTIVE + " = 1 "
                + "AND " + ROW_ID + " = '" + Long.toString(ScheduleId) +"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        ScheduledBrewSchema sBrew = new ScheduledBrewSchema();
        if (c != null) {
            c.moveToFirst();
            sBrew.setScheduleId(c.getLong(c.getColumnIndex(ROW_ID)));
            sBrew.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
            sBrew.setBrewId(c.getLong(c.getColumnIndex(BREW_ID)));
            sBrew.setBrewName(c.getString(c.getColumnIndex(BREW_NAME)));
            sBrew.setStartDate(c.getString(c.getColumnIndex(CREATED_ON)));
            sBrew.setAlertSecondaryDate(c.getString(c.getColumnIndex(SECONDARY_ALERT_DATE)));
            sBrew.setAlertBottleDate(c.getString(c.getColumnIndex(BOTTLE_ALERT_DATE)));
            sBrew.setEndBrewDate((c.getString(c.getColumnIndex(END_BREW_DATE))));
            sBrew.setOG(c.getDouble(c.getColumnIndex(ORIGINAL_GRAVITY)));
            sBrew.setFG((c.getDouble(c.getColumnIndex(FINAL_GRAVITY))));
            sBrew.setABV((c.getDouble(c.getColumnIndex(ABV))));
            sBrew.setActive((c.getInt(c.getColumnIndex(ACTIVE))));
            sBrew.setNotes((c.getString(c.getColumnIndex(NOTE))));
            sBrew.setColor((c.getString(c.getColumnIndex(STYLE_COLOR))));
            sBrew.setAlertSecondaryCalendarId((c.getLong(c.getColumnIndex(SECONDARY_ALERT_CALENDAR_ID))));
            sBrew.setAlertBottleCalendarId((c.getLong(c.getColumnIndex(BOTTLE_ALERT_CALENDAR_ID))));
            sBrew.setEndBrewCalendarId((c.getLong(c.getColumnIndex(END_BREW_CALENDAR_ID))));

        }
        c.close();
        return sBrew;
    }

    /*
* Get All Active Scheduled Brews
*/
    public List<ScheduledBrewSchema> getAllActiveScheduledBrews(long aUserId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ScheduledBrewSchema> sBrewList = new ArrayList<ScheduledBrewSchema>();
        String selectQuery = "SELECT " + ROW_ID + ",* FROM " + TABLE_BREWS_SCHEDULED + " WHERE "
                + ACTIVE + " = 1 "
                + "AND " + USER_ID + " = " + aUserId+" "
                + "ORDER BY " + CREATED_ON;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllActiveScheduledBrews Count["+c.getCount()+"]");
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                ScheduledBrewSchema sBrew = new ScheduledBrewSchema();
                sBrew.setScheduleId(c.getLong(c.getColumnIndex(ROW_ID)));
                sBrew.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
                sBrew.setBrewId(c.getLong(c.getColumnIndex(BREW_ID)));
                sBrew.setBrewName(c.getString(c.getColumnIndex(BREW_NAME)));
                sBrew.setStartDate(c.getString(c.getColumnIndex(CREATED_ON)));
                sBrew.setAlertSecondaryDate(c.getString(c.getColumnIndex(SECONDARY_ALERT_DATE)));
                sBrew.setAlertBottleDate(c.getString(c.getColumnIndex(BOTTLE_ALERT_DATE)));
                sBrew.setEndBrewDate((c.getString(c.getColumnIndex(END_BREW_DATE))));
                sBrew.setOG(c.getDouble(c.getColumnIndex(ORIGINAL_GRAVITY)));
                sBrew.setFG((c.getDouble(c.getColumnIndex(FINAL_GRAVITY))));
                sBrew.setABV((c.getDouble(c.getColumnIndex(ABV))));
                sBrew.setActive((c.getInt(c.getColumnIndex(ACTIVE))));
                sBrew.setNotes((c.getString(c.getColumnIndex(NOTE))));
                sBrew.setColor((c.getString(c.getColumnIndex(STYLE_COLOR))));
                sBrew.setAlertSecondaryCalendarId((c.getLong(c.getColumnIndex(SECONDARY_ALERT_CALENDAR_ID))));
                sBrew.setAlertBottleCalendarId((c.getLong(c.getColumnIndex(BOTTLE_ALERT_CALENDAR_ID))));
                sBrew.setEndBrewCalendarId((c.getLong(c.getColumnIndex(END_BREW_CALENDAR_ID))));

                // adding to Scheduled list if still active else set not active
                if(sBrew.getEndBrewDate().compareTo(getDateTime()) >= 0)
                    sBrewList.add(sBrew);
                else
                {
                    setBrewScheduledNotActive(sBrew.getScheduleId());
                    ScheduleNoteWriterHelper(sBrew);
                    // we also want to update brew to onTap
                    BrewSchema brewSchema = getBrew(sBrew.getBrewId(),sBrew.getUserId());
                    brewSchema.setBooleanOnTap(true);
                    updateABrew(brewSchema);
                }

            } while (c.moveToNext());
        }
        c.close();
        return sBrewList;
    }

    /*
* Updating a Brew Scheduled
*/
    public Boolean updateScheduledBrew(ScheduledBrewSchema aSBrew) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateABrew Name["+aSBrew.getBrewId()+"]");

        ContentValues values = new ContentValues();
        values.put(BREW_ID, aSBrew.getBrewId());
        values.put(USER_ID, aSBrew.getUserId());
        values.put(BREW_NAME, aSBrew.getBrewName());
        values.put(CREATED_ON, aSBrew.getStartDate());
        values.put(SECONDARY_ALERT_DATE, aSBrew.getAlertSecondaryDate());
        values.put(BOTTLE_ALERT_DATE, aSBrew.getAlertBottleDate());
        values.put(END_BREW_DATE, aSBrew.getEndBrewDate());
        values.put(ACTIVE, aSBrew.getActive());
        values.put(NOTE, aSBrew.getNotes());
        values.put(STYLE_COLOR, aSBrew.getColor());
        values.put(ORIGINAL_GRAVITY, aSBrew.getOG());
        values.put(FINAL_GRAVITY, aSBrew.getFG());
        values.put(ABV, aSBrew.getABV());
        values.put(SECONDARY_ALERT_CALENDAR_ID, aSBrew.getAlertSecondaryCalendarId());
        values.put(BOTTLE_ALERT_CALENDAR_ID, aSBrew.getAlertBottleCalendarId());
        values.put(END_BREW_CALENDAR_ID, aSBrew.getEndBrewCalendarId());

        // updating row
        int retVal = db.update(TABLE_BREWS_SCHEDULED, values, ROW_ID + " = ?",
                new String[] { Long.toString(aSBrew.getScheduleId()) });

        //Update brew
        if(!(retVal > 0) )
            return false;

        return true;
    }

    /*
    * Set A users Scheduled brew to not active
     */
    public void setBrewScheduledNotActive(long aScheduleId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "setBrewScheduledNotActive Schedule Id["+Long.toString(aScheduleId) +"]");

        ContentValues values = new ContentValues();
        values.put(ACTIVE, 0);

        // updating row
        db.update(TABLE_BREWS_SCHEDULED, values, ROW_ID + " = ?",
                new String[] { Long.toString(aScheduleId) });
    }

    /*
* delete A users Scheduled brew
 */
    public void deleteBrewScheduledById(long aScheduleId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteBrewScheduled Schedule Id["+Long.toString(aScheduleId) +"]");

        db.delete(TABLE_BREWS_SCHEDULED, ROW_ID + " = ?",
                new String[] { Long.toString(aScheduleId) });
    }

    /*
* delete A users Scheduled brew
*/
    public void deleteBrewScheduled(long aBrewId, long aUserId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteBrewScheduled Brew Name["+aBrewId+"] User Name["+aUserId+"]");

        db.delete(TABLE_BREWS_SCHEDULED, BREW_ID + " = ? AND "+ USER_ID +  " = ?",
                new String[] { Long.toString(aBrewId), Long.toString(aUserId)});
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
    //************************************App settings Table functions***************
                /*
    * add App setting
    */
    public boolean addAppSetting(AppSettingsSchema aAppSettingsSchema) {
        Log.e(LOG, "Insert: addAppSetting["+aAppSettingsSchema.getUserId()+", "+ aAppSettingsSchema.getSettingName() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, aAppSettingsSchema.getUserId());
        values.put(SETTING_NAME, aAppSettingsSchema.getSettingName());
        values.put(SETTING_VALUE, aAppSettingsSchema.getSettingValue());
        values.put(SETTING_SCREEN, aAppSettingsSchema.getSettingScreen());


        // insert row
        return db.insert(TABLE_APP_SETTINGS,null,values) > 0;
    }
    /*
    * add All App settings
    */
    public boolean addAllAppSettings(List <AppSettingsSchema> aAppSettingsSchema) {
        Log.e(LOG, "Insert: addAllAppSettings");

        if(aAppSettingsSchema.size() < 1)
            return true;

        // for each brew note try to add it to the DB
        for(Iterator<AppSettingsSchema> i = aAppSettingsSchema.iterator(); i.hasNext(); )
        {
            AppSettingsSchema appSettingsSchema = i.next();
            if(!addAppSetting(appSettingsSchema))
                return false;
        }

        return true;
    }

    /*
* getting App Settings by user Id and name
*/
    public AppSettingsSchema getAppSettingsBySettingName(AppSettingsSchema aAppSettingsSchema) {
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_APP_SETTINGS + " WHERE "
                + USER_ID + " = " +Long.toString(aAppSettingsSchema.getUserId())
                + " AND " + SETTING_NAME + " = '" + aAppSettingsSchema.getSettingName()+"' ";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        AppSettingsSchema appSettingsSchema = new AppSettingsSchema();
        Log.e(LOG, "getAppSettingsBySettingName Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();

            appSettingsSchema.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
            appSettingsSchema.setAppSetttingId(c.getLong(c.getColumnIndex(ROW_ID)));
            appSettingsSchema.setSettingName(c.getString(c.getColumnIndex(SETTING_NAME)));
            appSettingsSchema.setSettingValue(c.getString(c.getColumnIndex(SETTING_VALUE)));
            appSettingsSchema.setSettingScreen(c.getString(c.getColumnIndex(SETTING_SCREEN)));
        }

        c.close();
        return appSettingsSchema;
    }


    /*
    * getting all App Settings by user Id
    */
    public List<AppSettingsSchema> getAllAppSettingsByUserId(long aUserId) {
        List<AppSettingsSchema> appSettingsSchemaList = new ArrayList<AppSettingsSchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_APP_SETTINGS + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllAppSettingsByUserId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                AppSettingsSchema appSettingsSchema = new AppSettingsSchema();
                appSettingsSchema.setUserId(aUserId);
                appSettingsSchema.setAppSetttingId(c.getLong(c.getColumnIndex(ROW_ID)));
                appSettingsSchema.setSettingName(c.getString(c.getColumnIndex(SETTING_NAME)));
                appSettingsSchema.setSettingValue(c.getString(c.getColumnIndex(SETTING_VALUE)));
                appSettingsSchema.setSettingScreen(c.getString(c.getColumnIndex(SETTING_SCREEN)));

                // adding to boilList
                appSettingsSchemaList.add(appSettingsSchema);
            } while (c.moveToNext());
        }

        c.close();
        return appSettingsSchemaList;
    }

    /*
* Updating a App Setting
*/
    public Boolean updateAppSetting(AppSettingsSchema aAppSettingsSchema) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateAppSetting Name["+aAppSettingsSchema.getSettingName()+"]");

        ContentValues values = new ContentValues();
        values.put(USER_ID, aAppSettingsSchema.getUserId());
        values.put(SETTING_NAME, aAppSettingsSchema.getSettingName());
        values.put(SETTING_VALUE, aAppSettingsSchema.getSettingValue());
        values.put(SETTING_SCREEN, aAppSettingsSchema.getSettingScreen());

        // updating row
        int retVal = db.update(TABLE_APP_SETTINGS, values, ROW_ID + " = ?",
                new String[] { Long.toString(aAppSettingsSchema.getAppSetttingId()) });

        //Update brew
        if(!(retVal > 0) )
            return false;

        return true;
    }
    //************************************Inventory functions***************

    //************************************Hops functions***************
    /*
    * add Hops
    */
    public long CreateHops(HopsSchema aHopsSchema) {
        Log.e(LOG, "Insert: CreateHops["+aHopsSchema.getUserId()+", "+ aHopsSchema.getInventoryName() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, aHopsSchema.getUserId());
        values.put(BREW_ID, aHopsSchema.getBrewId());
        values.put(INVENTORY_NAME, aHopsSchema.getInventoryName());
        values.put(INVENTORY_QTY, aHopsSchema.getInvetoryQty());
        values.put(INVENTORY_UOFM, aHopsSchema.getInventoryUOfM());
        values.put(INVENTORY_AMOUNT, aHopsSchema.getAmount());
        values.put(INVENTORY_TYPE, aHopsSchema.getType());
        values.put(INVENTORY_USE, aHopsSchema.getUse());
        values.put(INVENTORY_IBU, aHopsSchema.getIBU());
        values.put(INVENTORY_AA, aHopsSchema.getAA());
        values.put(INVENTORY_TIME, aHopsSchema.getTime());


        // insert row
        return db.insert(TABLE_INVENTORY_HOPS,null,values);
    }


    /*
* getting Hops by Inventory Id
*/
    public InventorySchema getHops(HopsSchema aHopsSchema) {
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_HOPS + " WHERE "
                + ROW_ID + " = " +Long.toString(aHopsSchema.getInventoryId());

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        HopsSchema hopsSchema = new HopsSchema();
        Log.e(LOG, "getHops Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();

            hopsSchema.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
            hopsSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));
            hopsSchema.setBrewId(c.getLong(c.getColumnIndex(BREW_ID)));
            hopsSchema.setInventoryName(c.getString(c.getColumnIndex(INVENTORY_NAME)));
            hopsSchema.setInvetoryQty(c.getInt(c.getColumnIndex(INVENTORY_QTY)));
            hopsSchema.setAmount(c.getDouble(c.getColumnIndex(INVENTORY_AMOUNT)));
            hopsSchema.setInventoryUOfM(c.getString(c.getColumnIndex(INVENTORY_UOFM)));
            hopsSchema.setType(c.getString(c.getColumnIndex(INVENTORY_TYPE)));
            hopsSchema.setUse(c.getString(c.getColumnIndex(INVENTORY_USE)));
            hopsSchema.setIBU(c.getDouble(c.getColumnIndex(INVENTORY_IBU)));
            hopsSchema.setAA(c.getDouble(c.getColumnIndex(INVENTORY_AA)));
            hopsSchema.setTime(c.getInt(c.getColumnIndex(INVENTORY_TIME)));
        }

        c.close();
        return hopsSchema;
    }

    /*
    * getting all Hops by user Id
    */
    public List<InventorySchema> getAllHopsByUserId(long aUserId) {
        List<InventorySchema> hopsSchemaArrayList = new ArrayList<InventorySchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_HOPS + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId)
                + " AND " + BREW_ID + " = " + (-1);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllHopsByUserId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                HopsSchema hopsSchema = new HopsSchema();
                hopsSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));

                // adding to hopsSchemaArrayList
                hopsSchemaArrayList.add(getHops(hopsSchema));
            } while (c.moveToNext());
        }

        c.close();
        return hopsSchemaArrayList;
    }

    /*
* getting all Hops by user Id and brew Id
*/
    public List<InventorySchema> getAllHopsByUserIdandBrewId(long aUserId, long aBrewId) {
        List<InventorySchema> hopsSchemaArrayList = new ArrayList<InventorySchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_HOPS + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId)
                + " AND " + BREW_ID + " = " +Long.toString(aBrewId);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllHopsByUserIdandBrewId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                HopsSchema hopsSchema = new HopsSchema();
                hopsSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));

                // adding to hopsSchemaArrayList
                hopsSchemaArrayList.add(getHops(hopsSchema));
            } while (c.moveToNext());
        }

        c.close();
        return hopsSchemaArrayList;
    }

    /*
* Updating a Hops
*/
    public Boolean updateHops(HopsSchema aHopsSchema) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateHops Name["+aHopsSchema.getInventoryName()+"]");

        ContentValues values = new ContentValues();
        values.put(USER_ID, aHopsSchema.getUserId());
        values.put(BREW_ID, aHopsSchema.getBrewId());
        values.put(INVENTORY_NAME, aHopsSchema.getInventoryName());
        values.put(INVENTORY_QTY, aHopsSchema.getInvetoryQty());
        values.put(INVENTORY_AMOUNT, aHopsSchema.getAmount());
        values.put(INVENTORY_UOFM, aHopsSchema.getInventoryUOfM());
        values.put(INVENTORY_TYPE, aHopsSchema.getType());
        values.put(INVENTORY_USE, aHopsSchema.getUse());
        values.put(INVENTORY_IBU, aHopsSchema.getIBU());
        values.put(INVENTORY_AA, aHopsSchema.getAA());
        values.put(INVENTORY_TIME, aHopsSchema.getTime());

        // updating row
        int retVal = db.update(TABLE_INVENTORY_HOPS, values, ROW_ID + " = ?",
                new String[] { Long.toString(aHopsSchema.getInventoryId()) });

        //Update brew
        if(!(retVal > 0) )
            return false;

        return true;
    }

    /*
* Delete All brew Hops by Brew Id User Id
*/
    public void deleteHopsByBrewIdUserId( long aBrewId, long aUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteHopsByBrewIdUserId Name["+aBrewId+"]");

        db.delete(TABLE_INVENTORY_HOPS, BREW_ID + " = ? AND "+ USER_ID +  " = ? ",
                new String[] { Long.toString(aBrewId), Long.toString(aUserId)});
    }

    /*
* delete Hops by Id
*/
    public void deleteHopsById(long aHopsId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteHopsById Hops Id["+Long.toString(aHopsId) +"]");

        db.delete(TABLE_INVENTORY_HOPS, ROW_ID + " = ?",
                new String[] { Long.toString(aHopsId) });
    }

    //************************************Fermentables functions***************
    /*
    * add Fermentables
    */
    public long CreateFermentable(FermentablesSchema aFermentablesSchema) {
        Log.e(LOG, "Insert: CreateFermentable["+aFermentablesSchema.getUserId()+", "+ aFermentablesSchema.getInventoryName() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, aFermentablesSchema.getUserId());
        values.put(BREW_ID, aFermentablesSchema.getBrewId());
        values.put(INVENTORY_NAME, aFermentablesSchema.getInventoryName());
        values.put(INVENTORY_QTY, aFermentablesSchema.getInvetoryQty());
        values.put(INVENTORY_AMOUNT, aFermentablesSchema.getAmount());
        values.put(INVENTORY_UOFM, aFermentablesSchema.getInventoryUOfM());
        values.put(INVENTORY_PPG, aFermentablesSchema.getPoundPerGallon());
        values.put(INVENTORY_LOV, aFermentablesSchema.getLovibond());
        values.put(INVENTORY_BILL, aFermentablesSchema.getBill());

        // insert row
        return db.insert(TABLE_INVENTORY_FERMENTABLES,null,values);
    }


    /*
* getting Fermentables by Inventory Id
*/
    public InventorySchema getFermentable(FermentablesSchema aFermentablesSchema) {
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_FERMENTABLES + " WHERE "
                + ROW_ID + " = " +Long.toString(aFermentablesSchema.getInventoryId());

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        FermentablesSchema fermentablesSchema = new FermentablesSchema();
        Log.e(LOG, "getFermentable Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();

            fermentablesSchema.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
            fermentablesSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));
            fermentablesSchema.setBrewId(c.getLong(c.getColumnIndex(BREW_ID)));
            fermentablesSchema.setInventoryName(c.getString(c.getColumnIndex(INVENTORY_NAME)));
            fermentablesSchema.setInvetoryQty(c.getInt(c.getColumnIndex(INVENTORY_QTY)));
            fermentablesSchema.setAmount(c.getDouble(c.getColumnIndex(INVENTORY_AMOUNT)));
            fermentablesSchema.setInventoryUOfM(c.getString(c.getColumnIndex(INVENTORY_UOFM)));
            fermentablesSchema.setPoundPerGallon(c.getDouble(c.getColumnIndex(INVENTORY_PPG)));
            fermentablesSchema.setLovibond(c.getDouble(c.getColumnIndex(INVENTORY_LOV)));
            fermentablesSchema.setBill(c.getDouble(c.getColumnIndex(INVENTORY_BILL)));

        }

        c.close();
        return fermentablesSchema;
    }

    /*
* getting all Fermentables by user Id
*/
    public List<InventorySchema> getAllFermentablesByUserId(long aUserId) {
        List<InventorySchema> fermentablesSchemaArrayList = new ArrayList<InventorySchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_FERMENTABLES + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId)
                + " AND " + BREW_ID + " = " + (-1);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllFermentablesByUserId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                FermentablesSchema fermentablesSchema = new FermentablesSchema();
                fermentablesSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));

                // adding to boilList
                fermentablesSchemaArrayList.add(getFermentable(fermentablesSchema));
            } while (c.moveToNext());
        }

        c.close();
        return fermentablesSchemaArrayList;
    }

    /*
    * getting all Fermentables by user Id and Brew Id
    */
    public List<InventorySchema> getAllFermentablesByUserIdandBrewId(long aUserId,  long aBrewId) {
        List<InventorySchema> fermentablesSchemaArrayList = new ArrayList<InventorySchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_FERMENTABLES + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId)
                +" AND "+ BREW_ID + " = " +Long.toString(aBrewId);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllFermentablesByUserIdandBrewId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                FermentablesSchema fermentablesSchema = new FermentablesSchema();
                fermentablesSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));

                // adding to fermentablesSchemaArrayList
                fermentablesSchemaArrayList.add(getFermentable(fermentablesSchema));
            } while (c.moveToNext());
        }

        c.close();
        return fermentablesSchemaArrayList;
    }

    /*
* Updating a Fermentables
*/
    public Boolean updateFermentable(FermentablesSchema aFermentablesSchema) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateFermentable Name["+aFermentablesSchema.getInventoryName()+"]");

        ContentValues values = new ContentValues();
        values.put(USER_ID, aFermentablesSchema.getUserId());
        values.put(BREW_ID, aFermentablesSchema.getBrewId());
        values.put(INVENTORY_NAME, aFermentablesSchema.getInventoryName());
        values.put(INVENTORY_QTY, aFermentablesSchema.getInvetoryQty());
        values.put(INVENTORY_AMOUNT, aFermentablesSchema.getAmount());
        values.put(INVENTORY_UOFM, aFermentablesSchema.getInventoryUOfM());
        values.put(INVENTORY_PPG, aFermentablesSchema.getPoundPerGallon());
        values.put(INVENTORY_LOV, aFermentablesSchema.getLovibond());
        values.put(INVENTORY_BILL, aFermentablesSchema.getBill());


        // updating row
        int retVal = db.update(TABLE_INVENTORY_FERMENTABLES, values, ROW_ID + " = ?",
                new String[] { Long.toString(aFermentablesSchema.getInventoryId()) });

        //Update brew
        if(!(retVal > 0) )
            return false;

        return true;
    }

    /*
* Delete All brew Fermentables by Brew Id User Id
*/
    public void deleteFermentablesByBrewIdUserId( long aBrewId, long aUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteFermentablesByBrewIdUserId Name["+aBrewId+"]");

        db.delete(TABLE_INVENTORY_FERMENTABLES, BREW_ID + " = ? AND "+ USER_ID +  " = ? ",
                new String[] { Long.toString(aBrewId), Long.toString(aUserId)});
    }

    /*
* delete Fermentables by Id
*/
    public void deleteFermentableById(long aFermentableId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteFermentableById Id["+Long.toString(aFermentableId) +"]");

        db.delete(TABLE_INVENTORY_FERMENTABLES, ROW_ID + " = ?",
                new String[] { Long.toString(aFermentableId) });
    }

    //************************************Grains functions***************
    /*
    * add Grains
    */
    public long CreateGrain (GrainsSchema aGrainsSchema) {
        Log.e(LOG, "Insert: CreateGrain["+aGrainsSchema.getUserId()+", "+ aGrainsSchema.getInventoryName() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, aGrainsSchema.getUserId());
        values.put(BREW_ID, aGrainsSchema.getBrewId());
        values.put(INVENTORY_NAME, aGrainsSchema.getInventoryName());
        values.put(INVENTORY_QTY, aGrainsSchema.getInvetoryQty());
        values.put(INVENTORY_AMOUNT, aGrainsSchema.getAmount());
        values.put(INVENTORY_UOFM, aGrainsSchema.getInventoryUOfM());
        values.put(INVENTORY_PPG, aGrainsSchema.getPoundPerGallon());
        values.put(INVENTORY_LOV, aGrainsSchema.getLovibond());
        values.put(INVENTORY_BILL, aGrainsSchema.getBill());

        // insert row
        return db.insert(TABLE_INVENTORY_GRAINS,null,values);
    }


    /*
* getting Grains by Inventory Id
*/
    public InventorySchema getGrain(GrainsSchema aGrainsSchema) {
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_GRAINS + " WHERE "
                + ROW_ID + " = " +Long.toString(aGrainsSchema.getInventoryId());

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        GrainsSchema grainsSchema = new GrainsSchema();
        Log.e(LOG, "getGrains Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();

            grainsSchema.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
            grainsSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));
            grainsSchema.setBrewId(c.getLong(c.getColumnIndex(BREW_ID)));
            grainsSchema.setInventoryName(c.getString(c.getColumnIndex(INVENTORY_NAME)));
            grainsSchema.setInvetoryQty(c.getInt(c.getColumnIndex(INVENTORY_QTY)));
            grainsSchema.setAmount(c.getDouble(c.getColumnIndex(INVENTORY_AMOUNT)));
            grainsSchema.setInventoryUOfM(c.getString(c.getColumnIndex(INVENTORY_UOFM)));
            grainsSchema.setPoundPerGallon(c.getDouble(c.getColumnIndex(INVENTORY_PPG)));
            grainsSchema.setLovibond(c.getDouble(c.getColumnIndex(INVENTORY_LOV)));
            grainsSchema.setBill(c.getDouble(c.getColumnIndex(INVENTORY_BILL)));

        }

        c.close();
        return grainsSchema;
    }

    /*
    * getting all Grains by user Id
    */
    public List<InventorySchema> getAllGrainsByUserId(long aUserId) {
        List<InventorySchema> grainsSchemaArrayList = new ArrayList<InventorySchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_GRAINS + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId)
                + " AND " + BREW_ID + " = " + (-1);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllGrainsByUserId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                GrainsSchema grainsSchema = new GrainsSchema();
                grainsSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));

                // adding to grainsSchemaArrayList
                grainsSchemaArrayList.add(getGrain(grainsSchema));
            } while (c.moveToNext());
        }

        c.close();
        return grainsSchemaArrayList;
    }

    /*
* getting all Grains by user Id and Brew Id
*/
    public List<InventorySchema> getAllGrainsByUserIdandBrewId(long aUserId, long aBrewId) {
        List<InventorySchema> grainsSchemaArrayList = new ArrayList<InventorySchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_GRAINS + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId)
                + " AND " + BREW_ID + " = " +Long.toString(aBrewId);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllGrainsByUserIdandBrewId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                GrainsSchema grainsSchema = new GrainsSchema();
                grainsSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));

                // adding to grainsSchemaArrayList
                grainsSchemaArrayList.add(getGrain(grainsSchema));
            } while (c.moveToNext());
        }

        c.close();
        return grainsSchemaArrayList;
    }

    /*
* Updating a Grains
*/
    public Boolean updateGrain(GrainsSchema aGrainsSchema) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateGrain Name["+aGrainsSchema.getInventoryName()+"]");

        ContentValues values = new ContentValues();
        values.put(USER_ID, aGrainsSchema.getUserId());
        values.put(BREW_ID, aGrainsSchema.getBrewId());
        values.put(INVENTORY_NAME, aGrainsSchema.getInventoryName());
        values.put(INVENTORY_QTY, aGrainsSchema.getInvetoryQty());
        values.put(INVENTORY_AMOUNT, aGrainsSchema.getAmount());
        values.put(INVENTORY_UOFM, aGrainsSchema.getInventoryUOfM());
        values.put(INVENTORY_PPG, aGrainsSchema.getPoundPerGallon());
        values.put(INVENTORY_LOV, aGrainsSchema.getLovibond());
        values.put(INVENTORY_BILL, aGrainsSchema.getBill());


        // updating row
        int retVal = db.update(TABLE_INVENTORY_GRAINS, values, ROW_ID + " = ?",
                new String[] { Long.toString(aGrainsSchema.getInventoryId()) });

        //Update brew
        if(!(retVal > 0) )
            return false;

        return true;
    }

    /*
* Delete All brew Grains by Brew Id User Id
*/
    public void deleteGrainsByBrewIdUserId( long aBrewId, long aUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteGrainsByBrewIdUserId Name["+aBrewId+"]");

        db.delete(TABLE_INVENTORY_GRAINS, BREW_ID + " = ? AND "+ USER_ID +  " = ? ",
                new String[] { Long.toString(aBrewId), Long.toString(aUserId)});
    }

    /*
* delete Grains by Id
*/
    public void deleteGrainById(long aGrainId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteGrainById Id["+Long.toString(aGrainId) +"]");

        db.delete(TABLE_INVENTORY_GRAINS, ROW_ID + " = ?",
                new String[] { Long.toString(aGrainId) });
    }

    //************************************Yeast functions***************
    /*
    * add Yeast
    */
    public long CreateYeast(YeastSchema aYeastSchema) {
        Log.e(LOG, "Insert: CreateYeast["+aYeastSchema.getUserId()+", "+ aYeastSchema.getInventoryName() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, aYeastSchema.getUserId());
        values.put(BREW_ID, aYeastSchema.getBrewId());
        values.put(INVENTORY_NAME, aYeastSchema.getInventoryName());
        values.put(INVENTORY_QTY, aYeastSchema.getInvetoryQty());
        values.put(INVENTORY_AMOUNT, aYeastSchema.getAmount());
        values.put(INVENTORY_UOFM, aYeastSchema.getInventoryUOfM());
        values.put(INVENTORY_ATTENUATION, aYeastSchema.getAttenuation());
        values.put(INVENTORY_FLOCCULATION, aYeastSchema.getFlocculation());
        values.put(INVENTORY_OTL, aYeastSchema.getOptimumTempLow());
        values.put(INVENTORY_OTH, aYeastSchema.getOptimumTempHigh());
        values.put(INVENTORY_STARTER, aYeastSchema.getStarter());


        // insert row
        return db.insert(TABLE_INVENTORY_YEAST,null,values);
    }


    /*
* getting Yeast by Inventory Id
*/
    public InventorySchema getYeast(YeastSchema aYeastSchema) {
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_YEAST + " WHERE "
                + ROW_ID + " = " +Long.toString(aYeastSchema.getInventoryId());

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        YeastSchema yeastSchema = new YeastSchema();
        Log.e(LOG, "getYeast Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();

            yeastSchema.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
            yeastSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));
            yeastSchema.setBrewId(c.getLong(c.getColumnIndex(BREW_ID)));
            yeastSchema.setInventoryName(c.getString(c.getColumnIndex(INVENTORY_NAME)));
            yeastSchema.setInvetoryQty(c.getInt(c.getColumnIndex(INVENTORY_QTY)));
            yeastSchema.setAmount(c.getDouble(c.getColumnIndex(INVENTORY_AMOUNT)));
            yeastSchema.setInventoryUOfM(c.getString(c.getColumnIndex(INVENTORY_UOFM)));
            yeastSchema.setAttenuation(c.getDouble(c.getColumnIndex(INVENTORY_ATTENUATION)));
            yeastSchema.setFlocculation(c.getString(c.getColumnIndex(INVENTORY_FLOCCULATION)));
            yeastSchema.setOptimumTempLow(c.getDouble(c.getColumnIndex(INVENTORY_OTL)));
            yeastSchema.setOptimumTempHigh(c.getDouble(c.getColumnIndex(INVENTORY_OTH)));
            yeastSchema.setStarter(c.getInt(c.getColumnIndex(INVENTORY_STARTER)));

        }

        c.close();
        return yeastSchema;
    }

    /*
    * getting all Yeast by user Id
    */
    public List<InventorySchema> getAllYeastByUserId(long aUserId) {
        List<InventorySchema> yeastSchemaArrayList = new ArrayList<InventorySchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_YEAST + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId)
                + " AND " + BREW_ID + " = " + (-1);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllYeastByUserId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                YeastSchema yeastSchema = new YeastSchema();
                yeastSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));

                // adding to yeastSchemaArrayList
                yeastSchemaArrayList.add(getYeast(yeastSchema));
            } while (c.moveToNext());
        }

        c.close();
        return yeastSchemaArrayList;
    }

    /*
* getting all Yeast by user Id and Brew Id
*/
    public List<InventorySchema> getAllYeastByUserIdandBrewId(long aUserId, long  aBrewId) {
        List<InventorySchema> yeastSchemaArrayList = new ArrayList<InventorySchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_YEAST + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId)
                + " AND "+ BREW_ID + " = " +Long.toString(aBrewId);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllYeastByUserIdandBrewId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                YeastSchema yeastSchema = new YeastSchema();
                yeastSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));

                // adding to yeastSchemaArrayList
                yeastSchemaArrayList.add(getYeast(yeastSchema));
            } while (c.moveToNext());
        }

        c.close();
        return yeastSchemaArrayList;
    }

    /*
* Updating a Yeast
*/
    public Boolean updateYeast(YeastSchema aYeastSchema) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateYeast Name["+aYeastSchema.getInventoryName()+"]");

        ContentValues values = new ContentValues();
        values.put(USER_ID, aYeastSchema.getUserId());
        values.put(BREW_ID, aYeastSchema.getBrewId());
        values.put(INVENTORY_NAME, aYeastSchema.getInventoryName());
        values.put(INVENTORY_QTY, aYeastSchema.getInvetoryQty());
        values.put(INVENTORY_AMOUNT, aYeastSchema.getAmount());
        values.put(INVENTORY_UOFM, aYeastSchema.getInventoryUOfM());
        values.put(INVENTORY_ATTENUATION, aYeastSchema.getAttenuation());
        values.put(INVENTORY_FLOCCULATION, aYeastSchema.getFlocculation());
        values.put(INVENTORY_OTL, aYeastSchema.getOptimumTempLow());
        values.put(INVENTORY_OTH, aYeastSchema.getOptimumTempHigh());
        values.put(INVENTORY_STARTER, aYeastSchema.getStarter());

        // updating row
        int retVal = db.update(TABLE_INVENTORY_YEAST, values, ROW_ID + " = ?",
                new String[] { Long.toString(aYeastSchema.getInventoryId()) });

        //Update brew
        if(!(retVal > 0) )
            return false;

        return true;
    }

    /*
* Delete All brew Yeast by Brew Id User Id
*/
    public void deleteYeastByBrewIdUserId( long aBrewId, long aUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteYeastByBrewIdUserId Name["+aBrewId+"]");

        db.delete(TABLE_INVENTORY_YEAST, BREW_ID + " = ? AND "+ USER_ID +  " = ? ",
                new String[] { Long.toString(aBrewId), Long.toString(aUserId)});
    }

    /*
* delete Yeast by Id
*/
    public void deleteYeastById(long aYeastId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteYeastById Id["+Long.toString(aYeastId) +"]");

        db.delete(TABLE_INVENTORY_YEAST, ROW_ID + " = ?",
                new String[] { Long.toString(aYeastId) });
    }


    //************************************Equipment functions***************
    /*
    * add Equipment
    */
    public long CreateEquipment(EquipmentSchema aEquipmentSchema) {
        Log.e(LOG, "Insert: CreateEquipment["+aEquipmentSchema.getUserId()+", "+ aEquipmentSchema.getInventoryName() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, aEquipmentSchema.getUserId());
        values.put(BREW_ID, aEquipmentSchema.getBrewId());
        values.put(INVENTORY_NAME, aEquipmentSchema.getInventoryName());
        values.put(INVENTORY_QTY, aEquipmentSchema.getInvetoryQty());
        values.put(INVENTORY_AMOUNT, aEquipmentSchema.getAmount());
        values.put(INVENTORY_UOFM, aEquipmentSchema.getInventoryUOfM());

        // insert row
        return db.insert(TABLE_INVENTORY_EQUIPMENT,null,values);
    }


    /*
* getting Equipment by Inventory Id
*/
    public InventorySchema getEquipment(EquipmentSchema aEquipmentSchema) {
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_EQUIPMENT + " WHERE "
                + ROW_ID + " = " +Long.toString(aEquipmentSchema.getInventoryId());

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        EquipmentSchema equipmentSchema = new EquipmentSchema();
        Log.e(LOG, "getEquipment Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();

            equipmentSchema.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
            equipmentSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));
            equipmentSchema.setBrewId(c.getLong(c.getColumnIndex(BREW_ID)));
            equipmentSchema.setInventoryName(c.getString(c.getColumnIndex(INVENTORY_NAME)));
            equipmentSchema.setInvetoryQty(c.getInt(c.getColumnIndex(INVENTORY_QTY)));
            equipmentSchema.setAmount(c.getDouble(c.getColumnIndex(INVENTORY_AMOUNT)));
            equipmentSchema.setInventoryUOfM(c.getString(c.getColumnIndex(INVENTORY_UOFM)));
        }

        c.close();
        return equipmentSchema;
    }

    /*
    * getting all Equipment by user Id
    */
    public List<InventorySchema> getAllEquipmentByUserId(long aUserId) {
        List<InventorySchema> equipmentSchemaArrayList = new ArrayList<InventorySchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_EQUIPMENT + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId)
                + " AND " + BREW_ID + " = " + (-1);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllEquipmentByUserId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                EquipmentSchema equipmentSchema = new EquipmentSchema();
                equipmentSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));

                // adding to equipmentSchemaArrayList
                equipmentSchemaArrayList.add(getEquipment(equipmentSchema));
            } while (c.moveToNext());
        }

        c.close();
        return equipmentSchemaArrayList;
    }

    /*
* getting all Equipment by user Id and brew Id
*/
    public List<InventorySchema> getAllEquipmentByUserIdandBrewId(long aUserId, long aBrewId) {
        List<InventorySchema> equipmentSchemaArrayList = new ArrayList<InventorySchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_EQUIPMENT + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId)
                +" AND "+ BREW_ID + " = " +Long.toString(aBrewId);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllEquipmentByUserIdandBrewId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                EquipmentSchema equipmentSchema = new EquipmentSchema();
                equipmentSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));

                // adding to equipmentSchemaArrayList
                equipmentSchemaArrayList.add(getEquipment(equipmentSchema));
            } while (c.moveToNext());
        }

        c.close();
        return equipmentSchemaArrayList;
    }

    /*
* Updating a Equipment
*/
    public Boolean updateEquipment(EquipmentSchema aEquipmentSchema) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateEquipment Name["+aEquipmentSchema.getInventoryName()+"]");

        ContentValues values = new ContentValues();
        values.put(USER_ID, aEquipmentSchema.getUserId());
        values.put(BREW_ID, aEquipmentSchema.getBrewId());
        values.put(INVENTORY_NAME, aEquipmentSchema.getInventoryName());
        values.put(INVENTORY_QTY, aEquipmentSchema.getInvetoryQty());
        values.put(INVENTORY_AMOUNT, aEquipmentSchema.getAmount());
        values.put(INVENTORY_UOFM, aEquipmentSchema.getInventoryUOfM());

        // updating row
        int retVal = db.update(TABLE_INVENTORY_EQUIPMENT, values, ROW_ID + " = ?",
                new String[] { Long.toString(aEquipmentSchema.getInventoryId()) });

        //Update brew
        if(!(retVal > 0) )
            return false;

        return true;
    }

    /*
* Delete All brew Equipment by Brew Id User Id
*/
    public void deleteEquipmentByBrewIdUserId( long aBrewId, long aUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteEquipmentByBrewIdUserId Name["+aBrewId+"]");

        db.delete(TABLE_INVENTORY_EQUIPMENT, BREW_ID + " = ? AND "+ USER_ID +  " = ? ",
                new String[] { Long.toString(aBrewId), Long.toString(aUserId)});
    }

    /*
* delete Equipment by Id
*/
    public void deleteEquipmentById(long aEquipmentId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteEquipmentById Id["+Long.toString(aEquipmentId) +"]");

        db.delete(TABLE_INVENTORY_EQUIPMENT, ROW_ID + " = ?",
                new String[] { Long.toString(aEquipmentId) });
    }

    //************************************Other functions***************
    /*
    * add Other
    */
    public long CreateOther(OtherSchema aOtherSchema) {
        Log.e(LOG, "Insert: CreateOther["+aOtherSchema.getUserId()+", "+ aOtherSchema.getInventoryName() +"]");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, aOtherSchema.getUserId());
        values.put(BREW_ID, aOtherSchema.getBrewId());
        values.put(INVENTORY_NAME, aOtherSchema.getInventoryName());
        values.put(INVENTORY_QTY, aOtherSchema.getInvetoryQty());
        values.put(INVENTORY_AMOUNT, aOtherSchema.getAmount());
        values.put(INVENTORY_UOFM, aOtherSchema.getInventoryUOfM());

        // insert row
        return db.insert(TABLE_INVENTORY_OTHER,null,values);
    }


    /*
* getting Other by Inventory Id
*/
    public InventorySchema getOther(OtherSchema aOtherSchema) {
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_OTHER + " WHERE "
                + ROW_ID + " = " +Long.toString(aOtherSchema.getInventoryId());

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        OtherSchema otherSchema = new OtherSchema();
        Log.e(LOG, "getOther Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();

            otherSchema.setUserId(c.getLong(c.getColumnIndex(USER_ID)));
            otherSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));
            otherSchema.setBrewId(c.getLong(c.getColumnIndex(BREW_ID)));
            otherSchema.setInventoryName(c.getString(c.getColumnIndex(INVENTORY_NAME)));
            otherSchema.setInvetoryQty(c.getInt(c.getColumnIndex(INVENTORY_QTY)));
            otherSchema.setAmount(c.getDouble(c.getColumnIndex(INVENTORY_AMOUNT)));
            otherSchema.setInventoryUOfM(c.getString(c.getColumnIndex(INVENTORY_UOFM)));
        }

        c.close();
        return otherSchema;
    }

    /*
    * getting all Other by user Id
    */
    public List<InventorySchema> getAllOtherByUserId(long aUserId) {
        List<InventorySchema> otherSchemaArrayList = new ArrayList<InventorySchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_OTHER + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId)
                + " AND " + BREW_ID + " = " + (-1);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllOtherByUserId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                OtherSchema otherSchema = new OtherSchema();
                otherSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));

                // adding to otherSchemaArrayList
                otherSchemaArrayList.add(getOther(otherSchema));
            } while (c.moveToNext());
        }

        c.close();
        return otherSchemaArrayList;
    }

    /*
* getting all Other by user Id and brew Id
*/
    public List<InventorySchema> getAllOtherByUserIdandBrewId(long aUserId, long aBrewId) {
        List<InventorySchema> otherSchemaArrayList = new ArrayList<InventorySchema>();
        String selectQuery = "SELECT "+ROW_ID+",* FROM " + TABLE_INVENTORY_OTHER + " WHERE "
                + USER_ID + " = " +Long.toString(aUserId)
                +" AND "+ BREW_ID + " = " +Long.toString(aBrewId);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, "getAllOtherByUserIdandBrewId Count["+c.getCount()+"]");
        if (c.getCount() > 0 ) {
            c.moveToFirst();
            do {
                OtherSchema otherSchema = new OtherSchema();
                otherSchema.setInventoryId(c.getLong(c.getColumnIndex(ROW_ID)));

                // adding to otherSchemaArrayList
                otherSchemaArrayList.add(getOther(otherSchema));
            } while (c.moveToNext());
        }

        c.close();
        return otherSchemaArrayList;
    }

    /*
* Updating a Other
*/
    public Boolean updateOther(OtherSchema aOtherSchema) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "updateOther Name["+aOtherSchema.getInventoryName()+"]");

        ContentValues values = new ContentValues();
        values.put(USER_ID, aOtherSchema.getUserId());
        values.put(BREW_ID, aOtherSchema.getBrewId());
        values.put(INVENTORY_NAME, aOtherSchema.getInventoryName());
        values.put(INVENTORY_QTY, aOtherSchema.getInvetoryQty());
        values.put(INVENTORY_AMOUNT, aOtherSchema.getAmount());
        values.put(INVENTORY_UOFM, aOtherSchema.getInventoryUOfM());

        // updating row
        int retVal = db.update(TABLE_INVENTORY_OTHER, values, ROW_ID + " = ?",
                new String[] { Long.toString(aOtherSchema.getInventoryId()) });

        //Update brew
        if(!(retVal > 0) )
            return false;

        return true;
    }

    /*
* Delete All brew Other by Brew Id User Id
*/
    public void deleteOtherByBrewIdUserId( long aBrewId, long aUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteOtherByBrewIdUserId Name["+aBrewId+"]");

        db.delete(TABLE_INVENTORY_OTHER, BREW_ID + " = ? AND "+ USER_ID +  " = ? ",
                new String[] { Long.toString(aBrewId), Long.toString(aUserId)});
    }

    /*
* delete Other by Id
*/
    public void deleteOtherById(long aOtherId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "deleteOtherById Id["+Long.toString(aOtherId) +"]");

        db.delete(TABLE_INVENTORY_OTHER, ROW_ID + " = ?",
                new String[] { Long.toString(aOtherId) });
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
        Date date = new Date();
        return APPUTILS.dateFormat.format(date);
    }
/*
    Add Brew Note on completion of a schedule
 */
    private void ScheduleNoteWriterHelper(ScheduledBrewSchema aSBrew)
    {
        String brewNote = "******Auto Added by Scheduler******" +"\r\n "
                         + "Start Date: "+aSBrew.getStartDate() +"\r\n "
                         +"Orignal Gravity:  "+aSBrew.getOG() +"\r\n "
                         +"Final Gravity:  "+aSBrew.getFG();


        BrewNoteSchema nBrew = new BrewNoteSchema();
        nBrew.setBrewId(aSBrew.getBrewId());
        nBrew.setUserId(aSBrew.getUserId());
        nBrew.setCreatedOn(getDateTime());
        nBrew.setBrewNote(brewNote);

        addBrewNote(nBrew);
    }

    /*
    Add Inventory to brew on create
     */
    private boolean CreateBrewInventoryHelper(List<InventorySchema> aInventorySchemas)
    {
        boolean retVal = true;
        long rsts = -1;

        for (InventorySchema inventorySchema : aInventorySchemas) {

            if (inventorySchema instanceof HopsSchema)
                rsts=CreateHops((HopsSchema) inventorySchema);
            else if (inventorySchema instanceof FermentablesSchema)
                rsts=CreateFermentable((FermentablesSchema) inventorySchema);
            else if (inventorySchema instanceof GrainsSchema)
                rsts=CreateGrain((GrainsSchema) inventorySchema);
            else if (inventorySchema instanceof YeastSchema)
                rsts=CreateYeast((YeastSchema) inventorySchema);
            else if (inventorySchema instanceof EquipmentSchema)
                rsts=CreateEquipment((EquipmentSchema) inventorySchema);
            else if (inventorySchema instanceof OtherSchema)
                rsts=CreateOther((OtherSchema) inventorySchema);

            if(rsts < 0)
                retVal  = false;
        }

        return retVal;
    }
}
