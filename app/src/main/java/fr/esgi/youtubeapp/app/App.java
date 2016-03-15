package fr.esgi.youtubeapp.app;

import android.app.Application;
import android.util.Log;

import fr.esgi.youtubeapp.rest.YoutubeVideoRestClient;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private static App instance;

    private static YoutubeVideoRestClient youtubeVideoRestClient;

    public App(){
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        youtubeVideoRestClient = new YoutubeVideoRestClient();

        Log.i( TAG, "OnCreate()" );
    }

    /**
     * Return the RestClient object in order to use Retrofit library
     * @return youtubeVideoRestClient
     */
    public static YoutubeVideoRestClient getYoutubeRestClient(){
        return youtubeVideoRestClient;
    }
}
