package fr.esgi.youtubeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.esgi.youtubeapp.R;
import fr.esgi.youtubeapp.adapter.YoutubeRecyclerAdapter;
import fr.esgi.youtubeapp.app.App;
import fr.esgi.youtubeapp.database.DatabaseRepository;
import fr.esgi.youtubeapp.model.Video;
import fr.esgi.youtubeapp.utils.DeviceManagerUtils;
import fr.esgi.youtubeapp.utils.DividerItemDecoration;
import fr.esgi.youtubeapp.utils.RecyclerItemClickListener;
import fr.esgi.youtubeapp.utils.SharedHelperFavorites;
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
    private AppCompatActivity mActivity = null;
    private Context mContext = null;

    //Views and adapter
    private View rootView;
    private LinearLayout mLinearNoConnectionContainer;
    private ProgressBar mLoader;
    private RecyclerView contentRecyclerView;
    private YoutubeRecyclerAdapter contentAdapter;

    //Data
    private List<Video> contentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_content);

        mActivity = FetchContentActivity.this;
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

            if (mLoader != null) {
                mLoader.setVisibility(View.GONE);
                mLoader = null;

                mLinearNoConnectionContainer.setVisibility(View.VISIBLE);
            }

            Utils.showActionInToast(mContext, mContext.getResources().getString( R.string.pas_de_connexion ) );

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
                    contentRecyclerView.setHasFixedSize(true);
                    contentRecyclerView.setLayoutManager(mLayoutManager);
                    contentRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    //contentRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
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


                            View source_icon = view.findViewById(R.id.image_item);

                            //Create animation and start activity
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation( FetchContentActivity.this, source_icon, "thumb" );

                            ActivityCompat.startActivity( mActivity, intent, options.toBundle() );
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

        mLinearNoConnectionContainer = (LinearLayout) findViewById(R.id.no_connection_linear_container);

        mLoader = (ProgressBar) findViewById(R.id.youtube_content_loader);
        contentRecyclerView = (RecyclerView) findViewById(R.id.youtube_content_recyclerView);
    }

    private boolean isFavorite( Video contentVideo ){

        Log.i( TAG, "isFavourite Method" );

        boolean check = false;

        SharedHelperFavorites.init(this);

        ArrayList<String> array = SharedHelperFavorites.getVideosList();

        for(String items : array){
            Log.i( TAG, "item in list favorites : " + items );
        }

        if (array != null){
            if (array.contains(contentVideo.getVideoUrl())){
                check = true;
            }
            else{
                check = false;
            }
        }

        return check;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_show_favorites) {

            startActivity(new Intent( mContext, ShowFavActivity.class ));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
