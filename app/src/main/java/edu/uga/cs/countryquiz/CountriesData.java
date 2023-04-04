package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * CountriesData is a POJO (Plain Ol Java Object) Helper Class that is intended to be a link between
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

    // Constant variables that we're using when writing SQL queries
    private static final String TABLE_COUNTRIES = "countries";
    private static final String COLUMN_CONTINENT_NAME = "continent_name";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_COUNTRY_NAME = "country_name";
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

        String createResultsTableQuery = "CREATE TABLE IF NOT EXISTS results (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, result INTEGER NOT NULL)";

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
} // CountriesData Class