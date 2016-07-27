package fr.esgi.youtubeapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import fr.esgi.youtubeapp.R;
import fr.esgi.youtubeapp.model.Video;

/**
 * Created by MichaelWayne on 14/06/2016.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = ViewHolder.class.getSimpleName();

    private Context context;

    public CardView itemCardView;

    public ProgressBar itemLoader;
    public ImageView imageThumb;
    public TextView name, description;

    public ViewHolder(Context context, View view) {
        super(view);

        this.context = context;

        itemCardView = (CardView) view.findViewById(R.id.card_view_item);

        itemLoader = (ProgressBar) view.findViewById(R.id.loader_item);

        imageThumb = (ImageView) view.findViewById(R.id.image_item);

        name = (TextView) view.findViewById(R.id.name_item);
        description = (TextView) view.findViewById(R.id.description_item);
    }

    public void bind (Video itemYoutubeVideo){


        if (itemLoader != null){
            itemLoader.setVisibility(View.VISIBLE);
        }

        //Call Picasso to display the correct image in each row view item
        Picasso.with(context)
                .load(itemYoutubeVideo.getImageThumb())
                .into(imageThumb, new ImageLoadedCallback( itemLoader ) {
                    @Override
                    public void onSuccess() {
                        if (itemLoader != null) {
                            itemLoader.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {

                        imageThumb.setImageResource(R.mipmap.ic_launcher);

                        Log.e(TAG, "bandeau pictures - OOOOOOOHHH CA VA PAAAAAS LAAAAA !!!");

                        if (itemLoader != null) {
                            itemLoader.setVisibility(View.GONE);
                        }
                    }
                });

        name.setText(itemYoutubeVideo.getName());
        description.setText(itemYoutubeVideo.getDescription());

    }


    private class ImageLoadedCallback implements Callback {

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
