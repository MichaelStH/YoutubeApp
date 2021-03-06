package fr.esgi.youtubeapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.esgi.youtubeapp.R;
import fr.esgi.youtubeapp.model.Video;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
public class YoutubeRecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{

    private static final String TAG = YoutubeRecyclerAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater inflater;

    private List<Video> youtubeList;

    public YoutubeRecyclerAdapter(Context context, List<Video> youtubeList){

        this.context = context;
        this.youtubeList = youtubeList;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*
    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView itemCardView;

        public ProgressBar itemLoader;
        public ImageView imageThumb;
        public TextView name, description;

        public ViewHolder(View view) {
            super(view);

            itemCardView = (CardView) view.findViewById(R.id.card_view_item);

            itemLoader = (ProgressBar) view.findViewById(R.id.loader_item);

            imageThumb = (ImageView) view.findViewById(R.id.image_item);

            name = (TextView) view.findViewById(R.id.name_item);
            description = (TextView) view.findViewById(R.id.description_item);
        }
    }

    */

    @Override
    public int getItemCount() {
        return youtubeList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemYoutubeRow = inflater.inflate(R.layout.content_list_item_row, parent, false );

        return new ViewHolder(context, itemYoutubeRow);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Video itemYoutubeVideo = youtubeList.get( position );

        holder.bind(itemYoutubeVideo);

        /*
        if (holder.itemLoader != null){
            holder.itemLoader.setVisibility(View.VISIBLE);
        }

        //Call Picasso to display the correct image in each row view item
        Picasso.with(context)
                .load(itemYoutubeVideo.getImageThumb())
                .into(holder.imageThumb, new ImageLoadedCallback( holder.itemLoader ) {
                    @Override
                    public void onSuccess() {
                        if (holder.itemLoader != null) {
                            holder.itemLoader.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {

                        holder.imageThumb.setImageResource(R.mipmap.ic_launcher);

                        Log.e(TAG, "bandeau pictures - OOOOOOOHHH CA VA PAAAAAS LAAAAA !!!");

                        if (holder.itemLoader != null) {
                            holder.itemLoader.setVisibility(View.GONE);
                        }
                    }
                });

        holder.name.setText(itemYoutubeVideo.getName());
        holder.description.setText(itemYoutubeVideo.getDescription());
        */
    }

    /*
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
    */
}
