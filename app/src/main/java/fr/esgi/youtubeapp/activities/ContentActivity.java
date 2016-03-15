package fr.esgi.youtubeapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.esgi.youtubeapp.R;

/**
 * Created by MichaelWayne on 15/03/2016.
 */
public class ContentActivity extends AppCompatActivity {

    //TAG and context
    private static final String TAG = ContentActivity.class.getSimpleName();
    private Context mContext;

    //Views
    private ImageView imageThumb;
    private TextView titleTextView, descriptionTextView;

    //Bundle Args
    public static final String CONTENT_TITLE_ARG = "content_title";
    public static final String CONTENT_DESCRIPTION_ARG = "content_description";
    public static final String CONTENT_THUMBNAIL_ARG = "content_thumbnail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        mContext = this;

        Bundle extras = getIntent().getExtras();

        if (extras != null){

            titleTextView.setText( (String) extras.get(CONTENT_TITLE_ARG) );
            descriptionTextView.setText( (String) extras.get(CONTENT_DESCRIPTION_ARG) );

            Picasso.with(mContext)
                    .load((String)extras.get(CONTENT_THUMBNAIL_ARG))
                    .into(imageThumb);

        }
    }
}
