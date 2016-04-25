package fr.esgi.youtubeapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import fr.esgi.youtubeapp.R;

/**
 * Created by MichaelWayne on 25/04/2016.
 */
public class ShowFavActivity extends AppCompatActivity {

    private static final String TAG = ShowFavActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_fav);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TAG);
    }
}
