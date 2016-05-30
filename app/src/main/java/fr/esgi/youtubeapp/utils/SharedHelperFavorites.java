package fr.esgi.youtubeapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

import fr.esgi.youtubeapp.app.App;

/**
 * Created by MichaelWayne on 30/05/2016.
 */
public class SharedHelperFavorites {

    protected static SharedPreferences sharedPref;
    protected static SharedPreferences.Editor prefEditor;

    public static final String KEY_FAVORITE_VIDEO = "FAVORITE_VIDEO_";
    private static ArrayList<String> mVideosInFavorite = null;
    private static int iMaxSlotTaken = 0;


    public static void init( Context context ) {

        mVideosInFavorite = new ArrayList<String>( );

        sharedPref = context.getSharedPreferences(KEY_FAVORITE_VIDEO, Context.MODE_PRIVATE);

        int i = 0;
        String currentVideo = null;

        while( ( currentVideo = sharedPref.getString( KEY_FAVORITE_VIDEO + i, null ) ) != null ) {
            Log.e( "OHOH" , "init" );
            mVideosInFavorite.add( currentVideo );
            iMaxSlotTaken ++;
            i++;
        }
    }

    public static ArrayList<String> addVideoInFavorites( String videoURL ) {
        mVideosInFavorite.add( videoURL );
        prefEditor = sharedPref.edit();
        prefEditor.putString( KEY_FAVORITE_VIDEO + ( mVideosInFavorite.size( ) - 1), videoURL );
        prefEditor.commit();
        iMaxSlotTaken ++;

        return mVideosInFavorite;
    }

    public static ArrayList<String> getVideosList( ) {
        return mVideosInFavorite;
    }

    public static ArrayList<String> deleteVideoFromFavorites(String videoURL ) {
        mVideosInFavorite.remove( videoURL );

        for( int i = 0; i < iMaxSlotTaken; i ++ ) {
            if( i < mVideosInFavorite.size( ) ) {
                prefEditor = sharedPref.edit();
                prefEditor.putString( KEY_FAVORITE_VIDEO + i, mVideosInFavorite.get( 0 ) );
                prefEditor.commit();
            }
            else {
                prefEditor = sharedPref.edit();
                prefEditor.putString( KEY_FAVORITE_VIDEO + i, null );
                prefEditor.commit();
            }
        }

        iMaxSlotTaken --;
        return mVideosInFavorite;
    }
}