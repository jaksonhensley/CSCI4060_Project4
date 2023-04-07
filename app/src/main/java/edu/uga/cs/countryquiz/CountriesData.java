package edu.uga.cs.countryquiz;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
 * Schema:
 *
 * Table Name: countries
 * Columns: id INTEGER PRIMARY KEY AUTOINCREMENT, country_name TEXT NOT NULL, continent_name TEXT NOT NULL
 *
 * Table Name: results
 * Columns: id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, result INTEGER NOT NULL
 *
 * +------------------------+
 * |     TABLE_COUNTRIES    |
 * +------------------------+
 * | _id  INTEGER           |
 * | name TEXT NOT NULL     |
 * | continent TEXT NOT NULL|
 * |------------------------|
 * |         INDEXES        |
 * |------------------------|
 * | PK__COUNTRIES           |
 * +------------------------+
 *
 * +------------------------+
 * |     TABLE_RESULTS      |
 * +------------------------+
 * | _id  INTEGER           |
 * | date TEXT NOT NULL     |
 * | result FLOAT NOT NULL  |
 * |------------------------|
 * |         INDEXES        |
 * |------------------------|
 * | PK__RESULTS             |
 * +------------------------+
 *
 */


/**
 * CountriesData is a POJO (Plain Ole Java Object) Helper Class that is intended to be a link between
 * the SQLite Database and the Country Quiz app instance. It extends SQLiteOpenHelper and will be
 * the class that interacts with our SQLite Database.
 *
 * The CountriesData class is written with a singleton design philosophy, meaning that there can
 * ever only be 1 instance of this class at a time at any given point in the app's lifecycle.
 *
 * To use this class, call the CountriesData.getInstance() method. That method will return the active
 * instance of the class if one exists, and will create a new instance if one does not exist.
 *
 * NOTE: The constructor for the class is private, so the only way to interact with it is through
 * CountriesData.getInstance().
 */

public class CountriesData extends SQLiteOpenHelper {

    // Constant variables that we're using when writing SQL queries on the "countries" table
    private static final String TABLE_COUNTRIES = "countries";
    private static final String COLUMN_CONTINENT_NAME = "continent_name";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_COUNTRY_NAME = "country_name";

    // Constant variables that we're using when writing SQL queries on the "results" table
    private static final String TABLE_RESULTS = "results";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_RESULT = "result";

    // Constant variables that we're using for the overall Database
    private static final String DATABASE_NAME = "countries.db";
    private static final int DATABASE_VERSION = 1;

    // Instance of the class - there can only be one at a time
    private static CountriesData instance;

    // We're using TAG to provide a unique identifier for log messages related to the CountriesData class
    private static final String TAG = "CountriesData";

    // The global context instance variable defines context for the entire class, allowing us to use
    // It inside of overridden functions (namely onCreate()) where we cannot modify the method
    // Signature.
    //
    // To use the local scope of context (getInstance(), for example), simply use the
    // "context" variable. To use the global scope of context (onCreate()) use "this.context"
    private Context context;

    // This is our database
    private SQLiteDatabase db; // TODO: Do we need this?



    /**
     * Creates a new instance of the CountriesData class with the specified parameters.
     *
     * @param context The context for the activity or application that is using the Database.
     * @throws IllegalArgumentException if version is less than 1.
     */
    private CountriesData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    } // CountriesData Constructor

    /**
     * The getInstance() method allows our CountriesData class to comply with the principles of a
     * singleton class design, which means that there will only ever be one instance of this class
     * at any given point in our app's lifecycle.
     *
     * Upon being called, the getInstance() method will check to see if there is no active instance.
     * If no active instance exists, it will create one. The method then returns the active instance
     * of the class.
     *
     * WHY: This is important because we do not want to have more than one connection to the Database
     * at a time. This class has to be instantiated for our app to use it, but we don't want multiple
     * connections trying to read and write from the database all at one time. By guarding the class
     * with this method, we are able to ensure that there is only one connection between the app
     * and the database.
     *
     * @param context The context for the activity or application that is using the Database.
     * @return The instance of the CountriesData class
     */
    public static synchronized CountriesData getInstance(Context context) {
        if (instance == null) {
            instance = new CountriesData(context.getApplicationContext());
        } // if
        return instance;
    } // getInstance()

    @Override
    public void onCreate(SQLiteDatabase db) {
        // These strings are the queries that we will use to create the schema for the Database
        // NOTE: We're using IF NOT EXISTS as a safeguard to not write over the Database when the app
        // starts.
        //
        // Workflow for IF NOT EXISTS: The app starts and CountriesData.onCreate() is called ->
        // The queries to set up the Database are created ->
        // When the queries run, the IF NOT EXISTS guard checks to see if the table exists ->
        // If the tables exist, the method returns and doesn't write any data ->
        // If the tables do not exist, the method continues and creates the Database
        //
        // This satisfies the project's requirement that the app checks for an existing Database
        // Before creating one each time the app is opened
        String createCountriesTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_COUNTRIES +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COUNTRY_NAME + " TEXT NOT NULL, " +
                COLUMN_CONTINENT_NAME + " TEXT NOT NULL)";

        String createResultsTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_RESULTS +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_RESULT + " INTEGER NOT NULL)";
        // Executing the queries on the Database
        // These are wrapped in a try / catch block so that the app doesn't crash if an error occurs
        try {
            db.execSQL(createCountriesTableQuery);
            db.execSQL(createResultsTableQuery);
        } catch (SQLException e) {
            Log.e(TAG, "Error creating tables", e);
            e.printStackTrace();
        } // SQL Create Tables Queries try / catch block

        readDataFromCSV(db);

    } // onCreate()

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Since we're not changing the database schema, there's nothing to do here
        //
        // We still need to include this as an overridden method, however, since the CountriesData
        // Class extends SQLiteOpenHelper so the inclusion of this overridden method is required
    } // onUpgrade()

    private void readDataFromCSV(SQLiteDatabase db) {
        // Reading the data from the CSV into the Database
        InputStream inputStream = null;
        BufferedReader reader = null;
        String line;
        try {
            inputStream = this.context.getResources().openRawResource(R.raw.country_continent);
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Error opening CSV", e);
            e.printStackTrace();
        } // Initializing inputStream and reader variables try / catch block

        try {
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_COUNTRY_NAME, values[0]);
                contentValues.put(COLUMN_CONTINENT_NAME, values[1]);
                db.insert(TABLE_COUNTRIES, null, contentValues);
            } // while
        } catch (IOException e) {
            Log.e(TAG, "Error reading CSV into the Database", e);
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing the inputStream", e);
                e.printStackTrace();
            } // Closing inputStream try / catch block
        } // Reading CSV Values into Database try / catch block
    } // readDataFromCSV()

    /**
     * Returns an array of all unique continents in the database.
     * @return a String array containing the names of all unique continents in the database
     */
    public String[] getContinents(SQLiteDatabase db) {
        Cursor cursor = null;
        String[] continents = null;

        try {

            // NOTE: Using the DISTINCT keyword returns every unique continent in the data set
            cursor = db.rawQuery("SELECT DISTINCT " + COLUMN_CONTINENT_NAME + " FROM " + TABLE_COUNTRIES, null);
            continents = new String[cursor.getCount()];

            // Number of unique continents
            int continentIndex = cursor.getColumnIndex(COLUMN_CONTINENT_NAME);

            // If the column name doesn't exist in the result set, the getColumnIndex() method will return -1
            if (continentIndex >= 0) {
                int i = 0;
                while (cursor.moveToNext()) {
                    String continent = cursor.getString(continentIndex);
                    continents[i] = continent;
                    i++;
                } // while
            } // if
            cursor.close();
            return continents;
        } catch (Exception e) {
            Log.e(TAG, "Error getting the unique continent names from the Database", e);
            e.printStackTrace();
        } finally {
            cursor.close();
            return continents;
        }
    } // getContinents()

    /**
     * Retrieves all countries from the database and returns them as an array of Country objects.
     * @param db The SQLiteDatabase to retrieve data from.
     * @return An array of Country objects representing all countries in the database.
     */
    /**
     * Retrieve all countries from the database.
     *
     * @param db the SQLiteDatabase instance to retrieve the data from.
     * @return an array of Country objects.
     */
    public Country[] getCountries(SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COLUMN_COUNTRY_NAME + ", " + COLUMN_CONTINENT_NAME + " FROM " + TABLE_COUNTRIES, null);
            Country[] countries = new Country[cursor.getCount()];
            int countryNameIndex = cursor.getColumnIndex(COLUMN_COUNTRY_NAME);
            int continentNameIndex = cursor.getColumnIndex(COLUMN_CONTINENT_NAME);
            int i = 0;
            if (countryNameIndex >= 0 && continentNameIndex >= 0) {
                while (cursor.moveToNext()) {
                    String countryName = cursor.getString(countryNameIndex);
                    String continentName = cursor.getString(continentNameIndex);
                    Country country = new Country(countryName, continentName);
                    countries[i] = country;
                    i++;
                } // while
            } // if
            return countries;
        } catch (SQLiteException e) {
            Log.e(TAG, "Error retrieving countries from database: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    } // getCountries()


    /**
     * Create and/or open a database for reading. The database is not actually
     * opened until one of getWritableDatabase or
     * getReadableDatabase is called.
     *
     * This method should only be called from an SQLiteDatabase instance
     * created using getWritableDatabase or getReadableDatabase.
     *
     * Once opened successfully, the database is cached, so you can call this
     * method every time you need to read from the database. (Make sure to call
     * SQLiteDatabase.close() when you're done reading from the database.)
     *
     * @return a readable database object
     * @throws SQLiteException if the database cannot be opened
     */
    public SQLiteDatabase getReadableDatabase() {
        if (db == null) {
            db = super.getReadableDatabase();
        } // if
        return db;
    } // getReadableDatabase()

    /**
     * Create and/or open a database that will be used for reading and writing.
     * The first time this is called, the database will be opened and
     * onCreate, onUpgrade and/or onOpen will be
     * called.
     *
     * Once opened successfully, the database is cached, so you can call this
     * method every time you need to write to the database. (Make sure to call
     * SQLiteDatabase.close() when you're done writing to the database.)
     *
     * Errors such as bad permissions or a full disk may cause this operation
     * to fail, but future attempts may succeed if the problem is fixed.
     *
     * Database upgrade may take a long time, you should not
     * call this method from the application main thread, including from
     * ContentProvider.onCreate().
     *
     * @return a writable database object
     * @throws SQLiteException if the database cannot be opened for writing
     */
    public SQLiteDatabase getWritableDatabase() {
        if (db == null) {
            db = super.getWritableDatabase();
        } // if
        return db;
    } // getWritableDatabase()

    /**
     * Inserts a new quiz record into the database with the given date and result values.
     * @param date the date of the quiz as a string
     * @param result the result of the quiz as a float
     * @throws Exception if there was an error writing the record to the database
     */
    public void putRecord(String date, float result) {
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DATE, date);
            values.put(COLUMN_RESULT, result);
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLE_RESULTS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error writing the Quiz Report into the database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        } // Writing records from a quiz into the database try / catch block
    } // putRecord()

    /**
     * Retrieves all the quiz records from the "results" table in the database.
     * @param db the SQLiteDatabase object representing the database
     * @return an array of QuizRecord objects containing all the quiz records in the database, or
     * null if no records are found
     * @throws SQLException if there is an error executing the SQL query
     */
    public QuizRecord[] getAllQuizRecords(SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_RESULTS, null, null, null, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return null; // no records found
            }
            QuizRecord[] records = new QuizRecord[cursor.getCount()];
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            int resultIndex = cursor.getColumnIndex(COLUMN_RESULT);
            int i = 0;
            do {
                int id = cursor.getInt(idIndex);
                String date = cursor.getString(dateIndex);
                float result = cursor.getFloat(resultIndex);
                QuizRecord record = new QuizRecord(date, result);
                records[i] = record;
                i++;
            } while (cursor.moveToNext());
            return records;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            } // if
        } // Getting all quiz records try / catch block
    }// getAllQuizRecords()


} // CountriesData Class
