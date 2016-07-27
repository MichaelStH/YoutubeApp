package fr.esgi.youtubeapp.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.esgi.youtubeapp.R;
import fr.esgi.youtubeapp.model.Video;
import fr.esgi.youtubeapp.utils.ImageManagerUtils;
import fr.esgi.youtubeapp.utils.SharedHelperFavorites;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
public class ContentActivity extends AppCompatActivity {

    //TAG and context
    private static final String TAG = ContentActivity.class.getSimpleName();
    private Context mContext;

    //Views
    private View rootView;
    private ImageView imageThumb, imageThumbBlurred, addToFavoriteImage, launchVideoImage;
    private TextView titleTextView, descriptionTextView;

    //Bundle Arguments
    public static final String VIDEO_OBJECT_ARG = "content_video";

    /*
    public static final String ID_ARG = "content_id";
    public static final String NAME_ARG = "content_name";
    public static final String DESCRIPTION_ARG = "content_description";
    public static final String IMAGE_THUMB_URL_ARG = "content_image_thumb_url";
    public static final String VIDEO_URL_ARG = "content_video_url";
*/
    private Video item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        Where myBitmap is the Image from which you want to extract the color.
        Also for API 21 and above, you'll need to add the following flags if you're planning to color the status bar and navigation bar:
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        setContentView(R.layout.activity_content);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TAG);

        mContext = this;
        SharedHelperFavorites.init(this);

        Bundle extras = getIntent().getExtras();

        if ( extras == null ) {
            Log.e( TAG, "bundle is null - check the data you are trying to pass through please !" );
        }
        else {
            Log.e(TAG, "get the data one by one");

            /*
            id = extras.getString(ID_ARG);
            name = extras.getString(NAME_ARG);
            description = extras.getString(DESCRIPTION_ARG);
            thumb = extras.getString(IMAGE_THUMB_URL_ARG);
            video_url = extras.getString(VIDEO_URL_ARG);

            is_favorite = extras.getBoolean(IS_FAVORITE_ARG);
            */
            item = extras.getParcelable( VIDEO_OBJECT_ARG );
        }

        initViews();
        loadContent();
        setListeners();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e( "OHOH", "onResume"  );

        if ( !isFavourite() ){
            addToFavoriteImage.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        else {
            addToFavoriteImage.setImageResource(R.drawable.ic_star_black_24dp);
        }
    }

    private void initViews(){

        rootView = getWindow().getDecorView();

        imageThumb = (ImageView) findViewById(R.id.content_image_thumb);
        imageThumbBlurred = (ImageView) findViewById(R.id.content_image_thumb_blurred);
        addToFavoriteImage = (ImageView) findViewById(R.id.content_image_add_to_favourite);
        launchVideoImage = (ImageView) findViewById(R.id.content_image_launch_video);

        titleTextView = (TextView) findViewById(R.id.content_text_name);

        descriptionTextView = (TextView) findViewById(R.id.content_text_description);

    }

    private void loadContent(){

        //Load the background  thumb image
        Picasso.with(mContext)
                .load(item.getImageThumb())
                .into(imageThumbBlurred);

        ImageManagerUtils.setBlurredImage( mContext, imageThumbBlurred, 5 );

        ViewCompat.setTransitionName(imageThumb, "thumb");

        //Load the thumb image clicked before
        Picasso.with(mContext)
                .load(item.getImageThumb())
                .into(imageThumb, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "Image is correctly downloaded");

                        //retrouver le bitmap téléchargé par Picasso
                        Bitmap bitmap = ((BitmapDrawable) imageThumb.getDrawable()).getBitmap();

                        //demande à la palette de générer ses coleurs, de façon asynchrone
                        //afin de ne pas bloquer l'interface graphique
                        new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {

                                //lorsque la palette est générée, je l'utilise sur mes textViews
                                generatePalette(palette);
                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                });

        titleTextView.setText( item.getName() );
        descriptionTextView.setText( item.getDescription() );

        if ( !isFavourite() ){
            addToFavoriteImage.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        else {
            addToFavoriteImage.setImageResource(R.drawable.ic_star_black_24dp);
        }
    }


    private void setListeners(){

        this.rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        this.addToFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i( TAG, "add to favourite" );

                if( isFavourite() ){
                    removeFromFavorite( item.getVideoUrl() );
                    addToFavoriteImage.setImageResource(R.drawable.ic_star_border_black_24dp);
                }
                else{
                    saveInFavorite( item.getVideoUrl() );
                    addToFavoriteImage.setImageResource(R.drawable.ic_star_black_24dp);
                }


            }
        });

        this.launchVideoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i( TAG, "launchVideo" );
                watchYoutubeVideo( mContext, item.getVideoUrl() );
            }
        });
    }


    private boolean isFavourite(){

        Log.i( TAG, "isFavourite Method" );
        boolean check = false;

        ArrayList array = SharedHelperFavorites.getVideosList();

        if (array != null){
            if (array.contains(item.getVideoUrl())){
                check = true;
            }
            else{
                check = false;
            }
        }

        return check;
    }

    private boolean saveInFavorite( String videoURL ){

        boolean saved = false;
        Log.d( TAG, "add in shared preferences" );

        SharedHelperFavorites.addVideoInFavorites( videoURL );
        saved = true;

        return saved;
    }


    private boolean removeFromFavorite( String videoURL ){

        boolean removed = false;
        Log.d( TAG, "remove from shared preferences" );

        SharedHelperFavorites.deleteVideoFromFavorites(videoURL);
        removed = true;

        return removed;
    }

    /**
     * This function launch the Youtube App if it is installed , if it's not launch the broswer instead
     * @param context
     * @param videoUrl
     */
    public static void watchYoutubeVideo(Context context, String videoUrl){
        try {

            Log.i( TAG, "Launch video in Youtube App" );

            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(videoUrl) );
            context.startActivity( intent );

        } catch (ActivityNotFoundException e) {
            Log.e( TAG, "Error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generate palette in order to change toolbar's color
     * @param palette
     */
    public void generatePalette(Palette palette) {
        {
            Palette.Swatch muted = palette.getMutedSwatch();

            //il se peut que la palette ne génère pas tous les swatch
            if (muted != null) {
                //j'utilise getRgb() en tant que couleur de fond de ma toolbar
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(muted.getRgb()));
            }
        }
        {
            Palette.Swatch mutedDark = palette.getDarkMutedSwatch();
            if (mutedDark != null) {
                getWindow().setStatusBarColor(mutedDark.getRgb());
            }

        }
    }
}