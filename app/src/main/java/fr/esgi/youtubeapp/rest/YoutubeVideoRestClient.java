package fr.esgi.youtubeapp.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.esgi.youtubeapp.rest.service.YoutubeVideoApiService;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
public class YoutubeVideoRestClient {

    private static final String TAG = YoutubeVideoRestClient.class.getSimpleName();

    //BASE_ENDPOINT to fetch
    private static final String BASE_ENDPOINT = "https://raw.githubusercontent.com";

    private YoutubeVideoApiService apiService;

    /**
     * Constructor
     */
    public YoutubeVideoRestClient()
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .build();


        apiService = restAdapter.create(YoutubeVideoApiService.class);

        Log.i(TAG, "Construct");
    }

    public YoutubeVideoApiService getApiService(){
        Log.i(TAG, "Method public YoutubeVideoApiService getApiService()");
        return apiService;
    }
}
