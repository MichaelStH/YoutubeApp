package fr.esgi.youtubeapp.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by michael on 28/01/2016.
 */
public class Utils {

    /** This class can't be instantiated. */
    private Utils(){}

    public static  void showActionInToast(Context context, String textToShow){
        Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
    }

}
