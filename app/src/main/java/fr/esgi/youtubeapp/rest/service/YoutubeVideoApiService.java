package fr.esgi.youtubeapp.rest.service;

import java.util.List;

import fr.esgi.youtubeapp.model.Video;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
public interface YoutubeVideoApiService {

    @GET("/")
    void fetchYoutubeVideos(List<Video> youtubeVideosList, Callback cb);

}
