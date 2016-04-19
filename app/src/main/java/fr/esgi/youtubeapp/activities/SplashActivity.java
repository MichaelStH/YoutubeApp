package fr.esgi.youtubeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import fr.esgi.youtubeapp.R;

public class SplashActivity extends AppCompatActivity {

    private Context mContext;

    private View rootView;
    private ProgressBar progressBar;

    private int TIME_POST_DELAYED = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;
        rootView = getWindow().getDecorView();

        //create a new progress bar
        progressBar = null;
        if (rootView != null) {

            //Initialize the progressBar and display it
            progressBar = (ProgressBar) rootView.findViewById(R.id.splash_loader);
            progressBar.setVisibility(View.VISIBLE);
        }

        //create a handler to simulate the initialisation of the application
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Verify if the progressBar is showed
                if ( progressBar.isInLayout() ){
                    //Hide the progressBar
                    progressBar.setVisibility( View.GONE );
                }

                Intent intent = new Intent( mContext, FetchContentActivity.class );
                startActivity( intent );
                finish();

            }
        }, TIME_POST_DELAYED );

    }
}
