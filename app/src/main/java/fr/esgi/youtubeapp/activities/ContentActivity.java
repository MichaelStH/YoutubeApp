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

import fr.esgi.youtubeapp.R;
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

    private String id, name, description, thumb, video_url;

    private Video item;

    //Database
//    private YoutubeDatabaseRepository mRepo;
//    private YoutubeDatabaseOpenHelper mDBOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TAG);

        mContext = this;
//        mDBOpenHelper = new YoutubeDatabaseOpenHelper( mContext );

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
        }

        initViews();
        isFavourite();

        Picasso.with(mContext)
                .load(thumb)
                .into(imageThumb);

        titleTextView.setText( name );
        descriptionTextView.setText( description );

        addToFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i( TAG, "add to favourite" );

//                mRepo = new YoutubeDatabaseRepository(mContext);
                try{

                    item = createYoutubeObject(id, name);
                    Log.e("OHOH", item.getFavId() + " --- " + item.getName());

//                    mRepo.Open();

                    if ( !isFavourite() ){
//                        mRepo.SaveVideo( item );
                        addToFavoriteImage.setImageResource(R.drawable.ic_star_black_24dp);

                        Utils.showActionInToast(mContext, "Add to favorite");
                    }
                    else {
//                        mRepo.DeleteVideo( item.getFavId() );
                        addToFavoriteImage.setImageResource(R.drawable.ic_star_border_black_24dp);

                        Utils.showActionInToast( mContext, "Delete from favorite" );
                    }

//                    mRepo.Close();

                } catch (Exception e){
                    Log.e( TAG, "Erreur : " + e.getMessage() );
                }



            }
        });

        launchVideoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i( TAG, "launchVideo" );
                watchYoutubeVideo( mContext, video_url );
            }
        });

    }

    private void initViews(){

        imageThumb = (ImageView) findViewById(R.id.content_image_thumb);
        addToFavoriteImage = (ImageView) findViewById(R.id.content_image_add_to_favourite);
        launchVideoImage = (ImageView) findViewById(R.id.content_image_launch_video);

        titleTextView = (TextView) findViewById(R.id.content_text_name);

        descriptionTextView = (TextView) findViewById(R.id.content_text_description);

    }


    private Video createYoutubeObject(String id, String name){
        return new Video(id, name);
    }


    private boolean isFavourite(){

        Log.i( TAG, "isFavourite Method" );

        boolean check = false;
        /**

        mRepo = new YoutubeDatabaseRepository(mContext);

        try {

            //Open database
            mRepo.Open();

            //Check if the database is empty
            boolean isEmpty = mRepo.IsEmpty();

            //If empty
            if (isEmpty) {

                Log.e(TAG, "(YoutubeDatabaseRepository) YoutubeItem empty - No video Records");

            }
            //Or not
            else {

                Log.e(TAG, "(YoutubeDatabaseRepository) YoutubeItem not empty");
                //contactTempList = mRepo.GetAll();

                if (contactTempList != null) {
                    Log.d(TAG, "(getContactsDatabaseList) contactTempList not null");
                    if (contactTempList.size() != 0) {
                        Log.d(TAG, "(getContactsDatabaseList) contactTempList.size != 0");
                    }
                }

            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        mRepo.Close();
*/
        return check;
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