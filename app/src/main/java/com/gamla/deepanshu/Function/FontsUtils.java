package com.gamla.deepanshu.Function;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by Deepanshu on 05-03-2018.
 */

public class FontsUtils {
   public static String Fontname = "Grand_Aventure.otf";
    private Context context;
    public FontsUtils(Context context)
    {
        this.context = context;
    }

    public Typeface setFont(String fontname)

    {
        AssetManager am = context.getApplicationContext().getAssets();
        Typeface custom_font = Typeface.createFromAsset(am,  fontname);
        return custom_font;

    }

}
