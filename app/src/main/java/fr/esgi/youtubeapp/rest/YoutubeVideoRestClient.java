package fr.esgi.youtubeapp.rest;

import android.util.Log;

import fr.esgi.youtubeapp.rest.service.YoutubeVideoApiService;
import retrofit.RestAdapter;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
public class YoutubeVideoRestClient {

    private static final String TAG = YoutubeVideoRestClient.class.getSimpleName();

    private static final String BASE_ENDPOINT = "link to fetch";

    private YoutubeVideoApiService apiService;

    /**
     * Constructor
     */
    public YoutubeVideoRestClient(){
        Log.i(TAG, "Construct");
    }

    public RestAdapter getRestAdapter(   )
    {
        RestAdapter restAdapter = new RestAdapter.Builder( )
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_ENDPOINT)
                .build();

        return restAdapter;
    }

    public YoutubeVideoApiService getApiService(){
        Log.i(TAG, "Method public YoutubeVideoApiService getApiService()");
        return apiService;
    }
}
