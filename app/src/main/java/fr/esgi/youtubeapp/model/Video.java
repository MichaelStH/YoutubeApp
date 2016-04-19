package fr.esgi.youtubeapp.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
@Parcel
public class Video {

    private Integer db_id;

    @SerializedName("id")
    private String fav_id;
    private String name;
    private String description;

    @SerializedName("imageUrl")
    private String imageThumb;

    private String videoUrl;


    public Video(String fav_id, String name){
        this.fav_id = fav_id;
        this.name = name;
    }

    public Video(String fav_id, String name, String description, String imageThumb, String videoUrl){

        this.fav_id = fav_id;
        this.name = name;
        this.description = description;
        this.imageThumb = imageThumb;
        this.videoUrl = videoUrl;

    }

    //////////////////////////////////////////////////////////////////
    ////////////////////  GETTER & SETTER  ///////////////////////////
    //////////////////////////////////////////////////////////////////

    public Integer getId() {
        return db_id;
    }

    public void setId(Integer db_id) {
        this.db_id = db_id;
    }

    public String getFavId() {
        return fav_id;
    }

    public void setFavId(String fav_id) {
        this.fav_id = fav_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

}