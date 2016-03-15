package fr.esgi.youtubeapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import fr.esgi.youtubeapp.R;
import fr.esgi.youtubeapp.model.Video;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
public class YoutubeRecyclerAdapter extends RecyclerView.Adapter<YoutubeRecyclerAdapter.ViewHolder>{

    private static final String TAG = YoutubeRecyclerAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater inflater;

    private List<Video> youtubeList;

    public YoutubeRecyclerAdapter(Context context, List<Video> youtubeList){

        this.context = context;
        this.youtubeList = youtubeList;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView itemImageThumb;
        public ProgressBar itemLoader;
        public TextView itemTitle;

        public ViewHolder(View view) {
            super(view);

            //TODO : Add the views in item list row
        }
    }

    @Override
    public int getItemCount() {
        return youtubeList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemYoutubeRow = inflater.inflate(R.layout.content_list_item_row, parent, false );

        return new ViewHolder(itemYoutubeRow);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Video itemYoutubeVideo = youtubeList.get( position );

        if (holder.itemLoader != null){
            holder.itemLoader.setVisibility(View.VISIBLE);
        }

        holder.itemTitle.setText(itemYoutubeVideo.getTitle());

        //Call Picasso to display the correct image in each row view item
        Picasso.with(context)
                .load(itemYoutubeVideo.getThumbnail())
                .into(holder.itemImageThumb, new ImageLoadedCallback( holder.itemLoader ) {
                    @Override
                    public void onSuccess() {
                        if (holder.itemLoader != null) {
                            holder.itemLoader.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {

                        holder.itemImageThumb.setImageResource(R.mipmap.ic_launcher);

                        Log.e(TAG, "bandeau pictures - OOOOOOOHHH CA VA PAAAAAS LAAAAA !!!");

                        if (holder.itemLoader != null) {
                            holder.itemLoader.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private class ImageLoadedCallback implements Callback{

        ProgressBar mProgressBar;

        public  ImageLoadedCallback(ProgressBar progressBar){
            mProgressBar = progressBar;
        }

        @Override
        public void onSuccess() {
            Log.i("ImageLoadedCallback", "onSuccess : ");
        }

        @Override
        public void onError() {
            Log.i("ImageLoadedCallback", "onError");
        }
    }
}
