package fr.esgi.youtubeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import fr.esgi.youtubeapp.R;
import fr.esgi.youtubeapp.adapter.YoutubeRecyclerAdapter;
import fr.esgi.youtubeapp.app.App;
import fr.esgi.youtubeapp.model.Video;
import fr.esgi.youtubeapp.utils.DeviceManagerUtils;
import fr.esgi.youtubeapp.utils.RecyclerItemClickListener;
import fr.esgi.youtubeapp.utils.SharedHelperFavorites;
import fr.esgi.youtubeapp.utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MichaelWayne on 25/04/2016.
 */
public class ShowFavActivity extends AppCompatActivity {

    //TAG and context
    private static final String TAG = ShowFavActivity.class.getSimpleName();
    private AppCompatActivity mActivity = null;
    private Context mContext = null;

    //Views and adapter
    private View rootView;
    private LinearLayout mLinearNoConnectionContainer;
    private ProgressBar mLoader;
    private RecyclerView favRecyclerView;
    private YoutubeRecyclerAdapter contentAdapter;

    //Data
    private List<Video> contentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_fav);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TAG);

        mActivity = ShowFavActivity.this;
        mContext = this;

        //Initialize  the view
        initViews();

        //create a new progress bar for each image to be loaded
        mLoader = null;
        if (rootView != null) {
            mLoader = (ProgressBar) rootView.findViewById(R.id.show_fav_loader);
            mLoader.setVisibility(View.VISIBLE);
        }

        //Test the internet's connection
        if (!DeviceManagerUtils.isConnected(mContext)) {

            if (mLoader != null) {
                mLoader.setVisibility(View.GONE);
                mLoader = null;

                mLinearNoConnectionContainer.setVisibility(View.VISIBLE);
            }

            Utils.showActionInToast(mContext, mContext.getResources().getString(R.string.pas_de_connexion));

        } else {

            //Call Retrofit the fetch content and display it
            App.getYoutubeRestClient().getApiService().fetchYoutubeVideos(new Callback<List<Video>>() {

                @Override
                public void success(List<Video> videoItems, Response response) {

                    contentList = (ArrayList<Video>) videoItems;

                    ArrayList<Video> favArrayList = new ArrayList<Video>();
                    ArrayList<String> tmpArraylist = new ArrayList();

                    SharedHelperFavorites.init(mContext);
                    tmpArraylist = SharedHelperFavorites.getVideosList();

                    for ( int i = 0 ;  i < contentList.size() ; i++ ){

                        for( int k=0 ; k < tmpArraylist.size() ; k++ ){
                            if ( contentList.get(i).getVideoUrl().equals( tmpArraylist.get(k) ) ){
                                favArrayList.add( contentList.get(i) );
                            }
                        }
                    }

                    //create the adapter
                    contentAdapter = new YoutubeRecyclerAdapter(mContext, favArrayList);

                    //Set properties for the RecyclerView
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    favRecyclerView.setHasFixedSize(true);
                    favRecyclerView.setLayoutManager(mLayoutManager);
                    favRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    //contentRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
                    favRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            //Toast.makeText(mContext, position + " is selected", Toast.LENGTH_SHORT).show();

                            //Get the object's position in order to use the object and pass the data through the other activity
                            Video content = contentList.get(position);

                            Intent intent = new Intent(mContext, ContentActivity.class);

                            intent.putExtra(ContentActivity.ID_ARG, content.getFavId());
                            intent.putExtra(ContentActivity.NAME_ARG, content.getName());
                            intent.putExtra(ContentActivity.DESCRIPTION_ARG, content.getDescription());
                            intent.putExtra(ContentActivity.IMAGE_THUMB_URL_ARG, content.getImageThumb());
                            intent.putExtra(ContentActivity.VIDEO_URL_ARG, content.getVideoUrl());

                            View source_icon = view.findViewById(R.id.image_item);

                            //Create animation and start activity
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ShowFavActivity.this, source_icon, "thumb");

                            ActivityCompat.startActivity(mActivity, intent, options.toBundle());
                        }
                    }));

                    if (mLoader != null) {
                        mLoader.setVisibility(View.GONE);
                        mLoader = null;
                    }

                    if (favRecyclerView != null && !favRecyclerView.isInLayout()) {
                        favRecyclerView.setVisibility(View.VISIBLE);
                    }

                    favRecyclerView.setAdapter(contentAdapter);

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

        mLoader = (ProgressBar) findViewById(R.id.show_fav_loader);
        favRecyclerView = (RecyclerView) findViewById(R.id.show_fav_recyclerView);
    }
}
