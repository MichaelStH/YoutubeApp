package fr.esgi.youtubeapp.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by MichaelWayne on 24/04/2016.
 */
public abstract class Repository<T> implements IRepository<T> {

    private static final String TAG = Repository.class.getSimpleName();

    protected SQLiteDatabase database;
    protected SQLiteOpenHelper dbHelper;


    public Repository(){
    }


    public void Open() throws SQLException {

        database =  dbHelper.getWritableDatabase();
        Log.i(TAG, "Database opened");

    }

    public void Close(){
        dbHelper.close();
        database = null;
    }


    public SQLiteDatabase GetBDD(){
        return database;

    }

    public Boolean UpgradeVersion(int newVersion){
        return database.needUpgrade(newVersion);
    }

}
