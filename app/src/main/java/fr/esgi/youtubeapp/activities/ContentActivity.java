package fr.esgi.youtubeapp.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.List;

import fr.esgi.youtubeapp.R;
import fr.esgi.youtubeapp.database.DatabaseOpenHelper;
import fr.esgi.youtubeapp.database.DatabaseRepository;
import fr.esgi.youtubeapp.model.Video;
import fr.esgi.youtubeapp.utils.Utils;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
public class ContentActivity extends AppCompatActivity {

    //TAG and context
    private static final String TAG = ContentActivity.class.getSimpleName();
    private Context mContext;

    //Views
    private ImageView imageThumb, addToFavoriteImage, launchVideoImage;
    private TextView titleTextView, descriptionTextView;

    //Bundle Arguments
    public static final String ID_ARG = "content_id";
    public static final String NAME_ARG = "content_name";
    public static final String DESCRIPTION_ARG = "content_description";
    public static final String IMAGE_THUMB_URL_ARG = "content_image_thumb_url";
    public static final String VIDEO_URL_ARG = "content_video_url";
    public static final String IS_FAVORITE_ARG = "is_favorite";

    private String id, name, description, thumb, video_url;
    private boolean is_favorite = false;

    private Video item;

    //Database
    private DatabaseRepository mRepo;
    private DatabaseOpenHelper mDBOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TAG);

        mContext = this;
        mDBOpenHelper = new DatabaseOpenHelper( mContext );

        Bundle extras = getIntent().getExtras();

        if ( extras == null ) {
            Log.e( TAG, "bundle is null - check the data you are trying to pass through please !" );
        }
        else {
            Log.e(TAG, "get the data one by one");

            id = extras.getString(ID_ARG);
            name = extras.getString(NAME_ARG);
            description = extras.getString(DESCRIPTION_ARG);
            thumb = extras.getString(IMAGE_THUMB_URL_ARG);
            video_url = extras.getString(VIDEO_URL_ARG);

            is_favorite = extras.getBoolean(IS_FAVORITE_ARG);

            item = createYoutubeObject(id, name);
        }

        initViews();
        setListeners();
        loadContent();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e( "OHOH", "onResume"  );

        if ( !is_favorite ){
            addToFavoriteImage.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        else {
            addToFavoriteImage.setImageResource(R.drawable.ic_star_black_24dp);
        }
    }

    private void initViews(){

        imageThumb = (ImageView) findViewById(R.id.content_image_thumb);
        addToFavoriteImage = (ImageView) findViewById(R.id.content_image_add_to_favourite);
        launchVideoImage = (ImageView) findViewById(R.id.content_image_launch_video);

        titleTextView = (TextView) findViewById(R.id.content_text_name);

        descriptionTextView = (TextView) findViewById(R.id.content_text_description);

    }

    private void loadContent(){

        Picasso.with(mContext)
                .load(thumb)
                .into(imageThumb);

        titleTextView.setText( name );
        descriptionTextView.setText( description );

        if ( !is_favorite ){
            addToFavoriteImage.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        else {
            addToFavoriteImage.setImageResource(R.drawable.ic_star_black_24dp);
        }
    }


    private void setListeners(){

        this.addToFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i( TAG, "add to favourite" );

                if( is_favorite ){
                    removeFromFavorite( item );
                    is_favorite = false;
                    addToFavoriteImage.setImageResource(R.drawable.ic_star_border_black_24dp);
                }
                else{
                    saveInFavorite( item );
                    is_favorite = true;
                    addToFavoriteImage.setImageResource(R.drawable.ic_star_black_24dp);
                }


            }
        });

        this.launchVideoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i( TAG, "launchVideo" );
                watchYoutubeVideo( mContext, video_url );
            }
        });
    }


    private Video createYoutubeObject(String id, String name){
        return new Video(id, name);
    }


    private boolean isFavourite(){

        Log.i( TAG, "isFavourite Method" );

        boolean check = false;

        mRepo = new DatabaseRepository(mContext);

        try {

            //Open database
            mRepo.Open();

            //Check if the database is empty
            boolean isEmpty = mRepo.IsEmpty();

            //If empty
            if ( isEmpty ) {

                Log.e(TAG, "(DatabaseRepository) Video empty - No video Records");
                Utils.showActionInToast( mContext, "No video Recorded in database" );

            }
            //Or not
            else {

                Log.e(TAG, "(DatabaseRepository) YoutubeItem not empty");
                List<Video> videoTempList = mRepo.GetAll();


                Log.e( TAG, "youtubeTempList : " + videoTempList.toString() );

                if (videoTempList != null) {
                    Log.d(TAG, "(getYoutubeDatabaseList) youtubeTempList not null");

                    if (videoTempList.size() != 0) {

                        Log.d(TAG, "(getYoutubeDatabaseList) youtubeTempList.size != 0");
                        for ( Video video : videoTempList ){
                            if ( !video.getName().equals( item.getName() ) ){
                                Log.i( TAG, "Is not already in" );
                                //IS_FAVORITE = false;
                                check = false;
                            }
                            else{
                                Log.i( TAG, "Is already in" );
                                //IS_FAVORITE = true;
                                check = true;
                            }
                        }

                    }
                }

            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mRepo.Close();
        }

        return check;
    }

    private boolean saveInFavorite( Video video ){

        boolean saved = false;

        mRepo = new DatabaseRepository(mContext);

        try {

            mRepo.Open();
            mRepo.SaveVideo( video );

            saved = true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mRepo.Close();
        }

        return saved;
    }


    private boolean removeFromFavorite( Video video ){

        boolean removed = false;

        mRepo = new DatabaseRepository(mContext);

        try {

            mRepo.Open();
            mRepo.DeleteVideo( video.getFavId() );

            removed = true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mRepo.Close();
        }

        return removed;
    }

    public static void watchYoutubeVideo(Context context, String videoUrl){
        try {

            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(videoUrl) );
            context.startActivity( intent );

        } catch (ActivityNotFoundException e) {
            Log.e( TAG, "Erreur : " + e.getMessage());
        }
    }

}