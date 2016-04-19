package fr.esgi.youtubeapp.rest.service;

import java.util.List;

import fr.esgi.youtubeapp.model.Video;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
public interface YoutubeVideoApiService {

    //Method to retrieve the youtube content
    @GET("/florent37/MyYoutube/master/myyoutube.json")
    void fetchYoutubeVideos(Callback<List<Video>> cb);

}
