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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.esgi.youtubeapp.R;
import fr.esgi.youtubeapp.adapter.YoutubeRecyclerAdapter;
import fr.esgi.youtubeapp.app.App;
import fr.esgi.youtubeapp.model.Video;
import fr.esgi.youtubeapp.rest.service.YoutubeVideoApiService;
import fr.esgi.youtubeapp.utils.DividerItemDecoration;
import fr.esgi.youtubeapp.utils.RecyclerItemClickListener;
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
        contentRecyclerView = (RecyclerView) findViewById(R.id.youtube_content_recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(mLayoutManager);
        contentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contentRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        contentRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, position + " is selected", Toast.LENGTH_SHORT).show();

                Video content = contentList.get(position);

                Intent intent = new Intent(mContext, ContentActivity.class);
                intent.putExtra(ContentActivity.CONTENT_TITLE_ARG, content.getTitle());
                intent.putExtra(ContentActivity.CONTENT_DESCRIPTION_ARG, content.getDescription());
                intent.putExtra(ContentActivity.CONTENT_THUMBNAIL_ARG, content.getThumbnail());
                startActivity(intent);
            }
        }));

        //Call Retrofit the fetch content and display it
        App.getYoutubeRestClient().getRestAdapter().create(YoutubeVideoApiService.class).fetchYoutubeVideos(contentList, new Callback<List<Video>>() {

            @Override
            public void success(List<Video> videos, Response response) {
                //TODO : parse the data and put them in the RecyclerView

                contentAdapter = new YoutubeRecyclerAdapter(mContext, contentList);
                contentRecyclerView.setAdapter(contentAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage());
            }
        });


    }
}
