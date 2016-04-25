package fr.esgi.youtubeapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.esgi.youtubeapp.model.Video;

/**
 * Created by MichaelWayne on 24/04/2016.
 */
public class DatabaseRepository extends Repository<Video>{

    private static final String TAG = DatabaseRepository.class.getSimpleName();

    private static final String WHERE_ID_EQUALS = DatabaseOpenHelper.ID_COLUMN + " =?";


    public DatabaseRepository(Context context){
        super();

        dbHelper = new DatabaseOpenHelper( context );
    }


    /**
     * Allows to check if the Database is empty
     * @return
     */
    @Override
    public boolean IsEmpty() {
        boolean ok = false;

        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DatabaseOpenHelper.YOUTUBE_FAV_TABLE, null);
        if (cursor != null) {
            try
            {
                cursor.moveToFirst();                       // Always one row returned.
                if (cursor.getInt (0) == 0) {               // Zero count means empty table.
                    Log.i(TAG, "(DatabaseRepository) bdd is empty");
                    ok = true;
                }
                else
                {
                    Log.i(TAG, "(DatabaseRepository) bdd is not empty");
                    ok = false;
                }
            }
            catch(CursorIndexOutOfBoundsException e)
            {
                ok = true;
            }
        }

        cursor.close();
        Log.i(TAG, "(DatabaseRepository) bdd boolean value : " + String.valueOf(ok));

        return ok;
    }

    @Override
    public void SaveVideo(Video video) {

        ContentValues values = new ContentValues();

        values.put( DatabaseOpenHelper.FAV_ID_COLUMN, video.getFavId() );
        values.put( DatabaseOpenHelper.FAV_NAME_COLUMN, video.getName() );

        database.insert( DatabaseOpenHelper.YOUTUBE_FAV_TABLE, null, values );

        Log.e(TAG, "(SaveVideo) success");
    }

    @Override
    public void DeleteVideo(String id) {

        database.delete(
                DatabaseOpenHelper.YOUTUBE_FAV_TABLE,
                DatabaseOpenHelper.FAV_ID_COLUMN + "=?",
                new String[]{
                        String.valueOf(id)
                });

        Log.e(TAG, "(DeleteVideo) success");

    }

    @Override
    public List<Video> GetAll() {

        // Récupération de la liste des videos.
        Cursor cursor = database.query(
                DatabaseOpenHelper.YOUTUBE_FAV_TABLE,
                new String[]{
                        DatabaseOpenHelper.ID_COLUMN,
                        DatabaseOpenHelper.FAV_ID_COLUMN,
                        DatabaseOpenHelper.FAV_NAME_COLUMN
                },
                null, null, null, null, null);
        Log.i(TAG, "(DatabaseRepository) List<Video> getAll()");

        return ConvertCursorToListObject(cursor);

    }

    @Override
    public Video GetById(String id) {

        Log.i(TAG, "(GetById) ID : " + id);

        Cursor cursor = database.query(
                DatabaseOpenHelper.YOUTUBE_FAV_TABLE,
                new String[] {
                        DatabaseOpenHelper.ID_COLUMN,
                        DatabaseOpenHelper.FAV_ID_COLUMN,
                        DatabaseOpenHelper.FAV_NAME_COLUMN
                },
                DatabaseOpenHelper.ID_COLUMN + "=?",
                new String[] { String.valueOf(id) }, null, null, null,null);

        Log.i(TAG, "return ConvertCursorToObject(cursor)");

        return ConvertCursorToObject(cursor);

    }

    @Override
    public Video GetObject(String id) {

        Cursor cursor = database.query(DatabaseOpenHelper.YOUTUBE_FAV_TABLE,
                new String[] {
                        DatabaseOpenHelper.ID_COLUMN,
                        DatabaseOpenHelper.FAV_ID_COLUMN,
                        DatabaseOpenHelper.FAV_NAME_COLUMN
                },
                DatabaseOpenHelper.ID_COLUMN + "=?",
                new String[] {
                        String.valueOf(id)
                }, null, null, null,null);
        Log.i(TAG, "Video getObject(test 1)");

        if (cursor != null)
            cursor.moveToFirst();

        Log.i(TAG, "Video getObject(test 2)");

        Log.i(TAG, "Video getObject(test 3)");
        Log.i(TAG, "cursor.getCount() = " + cursor.getCount());
        Log.i(TAG, Integer.parseInt(cursor.getString(0)) + "");
        Log.i(TAG, cursor.getString(1));
        Log.i(TAG, cursor.getString(2));


        Video video = new Video(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2));

        Log.i(TAG, "Video getObject(int id)");

        return video;
    }

    /**
     * Convert a cursor into a contact list
     * @param c
     * @return
     */
    @Override
    public List<Video> ConvertCursorToListObject(Cursor c) {

        List<Video> videoList = new ArrayList<Video>();

        // Si la liste est vide
        if (c.getCount() == 0)
            return videoList;

        // position sur le premeir item
        c.moveToFirst();

        // Pour chaque item
        do {

            Video video = ConvertCursorToObject(c);

            videoList.add(video);
        } while (c.moveToNext());

        // Fermeture du curseur
        c.close();

        Log.i(TAG, "List<Video> ConvertCursorToListObject(Cursor c)");

        return videoList;
    }

    /**
     * Method used by ConvertCursorToObject and ConvertCursorToListObject </br>
     * Allows to convert a cursor to a Video object
     * @param c
     * @return
     */
    @Override
    public Video ConvertCursorToObject(Cursor c) {

        Video video = new Video(
                c.getString(DatabaseOpenHelper.NUM_COLUMN_FAV_ID),
                c.getString(DatabaseOpenHelper.NUM_COLUMN_FAV_NAME));
        video.setId(c.getInt(DatabaseOpenHelper.NUM_COLUMN_ID));

        Log.i(TAG, "Video ConvertCursorToObject(Cursor c)");
        return video;
    }

    /**
     * Convert a cursor to a Video object
     * @param c
     * @return
     */
    @Override
    public Video ConvertCursorToOneObject(Cursor c) {

        c.moveToFirst();
        Video video = ConvertCursorToObject(c);
        c.close();

        return video;
    }
}
