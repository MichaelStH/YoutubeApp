package fr.esgi.youtubeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.esgi.youtubeapp.R;
import fr.esgi.youtubeapp.adapter.YoutubeRecyclerAdapter;
import fr.esgi.youtubeapp.app.App;
import fr.esgi.youtubeapp.database.DatabaseRepository;
import fr.esgi.youtubeapp.model.Video;
import fr.esgi.youtubeapp.rest.YoutubeVideoRestClient;
import fr.esgi.youtubeapp.rest.service.YoutubeVideoApiService;
import fr.esgi.youtubeapp.utils.DeviceManagerUtils;
import fr.esgi.youtubeapp.utils.DividerItemDecoration;
import fr.esgi.youtubeapp.utils.RecyclerItemClickListener;
import fr.esgi.youtubeapp.utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
public class FetchContentActivity extends AppCompatActivity {

    //TAG and context
    private static final String TAG = FetchContentActivity.class.getSimpleName();
    private Context mContext = null;

    //Views and adapter
    private View rootView;
    private ProgressBar mLoader;
    private RecyclerView contentRecyclerView;
    private YoutubeRecyclerAdapter contentAdapter;

    //Data
    private List<Video> contentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_content);

        mContext = this;

        //Initialize  the view
        initViews();

        //create a new progress bar for each image to be loaded
        mLoader = null;
        if (rootView != null) {
            mLoader = (ProgressBar) rootView.findViewById(R.id.youtube_content_loader);
            mLoader.setVisibility(View.VISIBLE);
        }

        //Test the internet's connection
        if( !DeviceManagerUtils.isConnected(mContext) ) {

            Utils.showActionInToast(mContext, "You are not connected to the internet");
        }
        else {

            //Call Retrofit the fetch content and display it
            App.getYoutubeRestClient().getApiService().fetchYoutubeVideos(new Callback<List<Video>>() {

                @Override
                public void success(List<Video> videoItems, Response response) {

                    contentList = (ArrayList<Video>) videoItems;

                    //create the adapter
                    contentAdapter = new YoutubeRecyclerAdapter(mContext, contentList);

                    //Set properties for the RecyclerView
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    contentRecyclerView.setLayoutManager(mLayoutManager);
                    contentRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    contentRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
                    contentRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            //Toast.makeText(mContext, position + " is selected", Toast.LENGTH_SHORT).show();

                            //Get the object's position in order to use the object and pass the data through the other activity
                            Video content = contentList.get( position );

                            Intent intent = new Intent(mContext, ContentActivity.class);

                            intent.putExtra(ContentActivity.ID_ARG, content.getFavId());
                            intent.putExtra(ContentActivity.NAME_ARG, content.getName());
                            intent.putExtra(ContentActivity.DESCRIPTION_ARG, content.getDescription());
                            intent.putExtra(ContentActivity.IMAGE_THUMB_URL_ARG, content.getImageThumb());
                            intent.putExtra(ContentActivity.VIDEO_URL_ARG, content.getVideoUrl());

                            intent.putExtra(ContentActivity.IS_FAVORITE_ARG, isFavorite( content ) );

                            startActivity(intent);
                        }
                    }));

                    if ( mLoader != null ){
                        mLoader.setVisibility(View.GONE);
                        mLoader = null;
                    }

                    if ( contentRecyclerView != null && !contentRecyclerView.isInLayout()){
                        contentRecyclerView.setVisibility(View.VISIBLE);
                    }

                    contentRecyclerView.setAdapter(contentAdapter);

                }

                @Override
                public void failure(RetrofitError error) {

                    if (mLoader != null) {
                        mLoader.setVisibility(View.GONE);
                        mLoader = null;
                    }

                    Log.e(TAG, error.getMessage());
                }
            });
        }
    }

    private void initViews(){

        rootView = getWindow().getDecorView();

        mLoader = (ProgressBar) findViewById(R.id.youtube_content_loader);
        contentRecyclerView = (RecyclerView) findViewById(R.id.youtube_content_recyclerView);
    }

    private boolean isFavorite( Video contentVideo ){

        Log.i( TAG, "isFavourite Method" );

        boolean check = false;

        DatabaseRepository mRepo = new DatabaseRepository(mContext);

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
                            if ( !video.getName().equals( contentVideo.getName() ) ){
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
}
