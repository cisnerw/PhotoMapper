package com.epitech.paul.photomapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 23/02/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "photomap";
    private static final String TABLE_PICTURES = "pictures";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATE = "date";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_IMAGE_PATH = "image";

    private static DatabaseHandler instance = null;

    public static DatabaseHandler getInstance()
    {
        return instance;
    }

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        instance = this;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_PICTURES_TABLE = "CREATE TABLE " + TABLE_PICTURES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_LONGITUDE + " DOUBLE,"
                + KEY_LATITUDE + " DOUBLE,"
                + KEY_IMAGE_PATH + " IMAGE)";
        db.execSQL(CREATE_PICTURES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURES);

        // Create tables again
        onCreate(db);
    }

    // Adding new picture
    public void addPicture(LocatedPicture locatedPicture)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, locatedPicture.getTitle());
        values.put(KEY_ADDRESS, locatedPicture.getAddress());
        values.put(KEY_LONGITUDE, locatedPicture.getLongitude());
        values.put(KEY_LATITUDE, locatedPicture.getLatitude());
        values.put(KEY_IMAGE_PATH, locatedPicture.getPicturePath().toString());

        // Inserting Row
        db.insert(TABLE_PICTURES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single picture
    public LocatedPicture getContact(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PICTURES,
                new String[]
                        {
                                KEY_ID,         // 0
                                KEY_TITLE,      // 1
                                KEY_ADDRESS,    // 2
                                KEY_LONGITUDE,  // 3
                                KEY_LATITUDE,   // 4
                                KEY_IMAGE_PATH  // 5
                        },
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }

        LocatedPicture locatedPicture = new LocatedPicture(
                cursor.getString(5),
                cursor.getString(1),
                cursor.getString(2),
                Double.parseDouble(cursor.getString(3)),
                Double.parseDouble(cursor.getString(4)));
        return locatedPicture;
    }

    public List<LocatedPicture> getAllPictures()
    {
        List<LocatedPicture> picturesList = new ArrayList<LocatedPicture>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PICTURES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                LocatedPicture locatedPicture = new LocatedPicture(
                        cursor.getString(5),
                        cursor.getString(1),
                        cursor.getString(2),
                        Double.parseDouble(cursor.getString(3)),
                        Double.parseDouble(cursor.getString(4)));

                File imgFile = new File(cursor.getString(5));
                if (imgFile.exists() == false)
                {
                    deletePicture(locatedPicture);
                }
                else
                {
                    picturesList.add(locatedPicture);
                }
            } while (cursor.moveToNext());
        }

        return picturesList;
    }

    public int getPicturesCount()
    {
        String countQuery = "SELECT  * FROM " + TABLE_PICTURES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updatePicture(LocatedPicture locatedPicture)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, locatedPicture.getTitle());
        values.put(KEY_ADDRESS, locatedPicture.getAddress());
        values.put(KEY_LONGITUDE, locatedPicture.getLongitude());
        values.put(KEY_LATITUDE, locatedPicture.getLatitude());
        values.put(KEY_IMAGE_PATH, locatedPicture.getPicturePath().toString());

        // updating row
        return db.update(TABLE_PICTURES, values, KEY_IMAGE_PATH + " = ?",
                new String[] { String.valueOf(locatedPicture.getPicturePath()) });
    }

    public void deletePicture(LocatedPicture locatedPicture)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PICTURES, KEY_IMAGE_PATH + " = ?",
                new String[] { String.valueOf(locatedPicture.getPicturePath()) });
        db.close();
    }
}
