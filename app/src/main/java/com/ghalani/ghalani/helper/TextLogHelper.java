package com.ghalani.ghalani.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Amanze on 6/3/2016.
 */
public class TextLogHelper {

    public static void log(String txt){
        Log.e("ghalani", txt);
    }

    public static void toast(Context ctx, String txt, Boolean isLong){
        Toast.makeText(ctx, txt, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }
}
