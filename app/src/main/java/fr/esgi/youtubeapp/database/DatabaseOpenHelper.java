package fr.esgi.youtubeapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by MichaelWayne on 24/04/2016.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseOpenHelper.class.getSimpleName();

    private Context mContext;

    private static final String DATABASE_NAME = "youtubedb";
    private static final int DATABASE_VERSION = 1;

    public static final String YOUTUBE_FAV_TABLE = "youtube_fav";

    public static final String ID_COLUMN = "id";
    public static final int NUM_COLUMN_ID = 0;
    public static final String FAV_ID_COLUMN = "fav_id";
    public static final int NUM_COLUMN_FAV_ID = 1;
    public static final String FAV_NAME_COLUMN = "fav_name";
    public static final int NUM_COLUMN_FAV_NAME = 2;

    public static final String CREATE_YOUTUBE_TABLE = "CREATE TABLE "
            + YOUTUBE_FAV_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY, "
            + FAV_ID_COLUMN + " TEXT, "
            + FAV_NAME_COLUMN + " TEXT" + ")";


    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_YOUTUBE_TABLE);

        Log.d(TAG, "Table successfully created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Lorsque l'on change le numéro de version de la base on supprime la
        // table puis on la recrée
        if (newVersion > DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + YOUTUBE_FAV_TABLE + ";");
            db.execSQL("DROP TABLE IF EXISTS android_metadata");
            onCreate(db);
        }

        Log.i(TAG, "Database upgraded");
    }
}
