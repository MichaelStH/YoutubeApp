package fr.esgi.youtubeapp.model;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
public class Video {

    public String title;
    public String description;
    public String thumbnail;

    public Video (String title, String description, String thumbnail){
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
