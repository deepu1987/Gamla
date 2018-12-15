package com.gamla.deepanshu.Function;

import android.app.Activity;
import android.graphics.Color;

import com.gamla.deepanshu.gamla.R;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

/**
 * Created by Deepanshu on 02-05-2018.
 */

public class CustomAlert {
    private Activity ac;
    public boolean isfinish = false;
    String sucess = "true";
    public CustomAlert(Activity ac)
    {
        this.ac = ac;
    }


    public void ShowDialog(String title,String message,boolean finish,String sucess)
    {
        isfinish = finish;
        int backgroundcolor;
        int icon;
        if(sucess.equalsIgnoreCase("true"))
        {
            backgroundcolor = ac.getResources().getColor(R.color.colorPrimaryDark);
            icon = R.drawable.ic_check_black_24dp;
        }
        else
        {
            backgroundcolor = Color.RED;
            icon = R.drawable.ic_error_outline_black_24dp;
        }

        new FancyAlertDialog.Builder(ac)
                .setTitle(title)
                .setBackgroundColor(backgroundcolor)  //Don't pass R.color.colorvalue
                .setMessage(message)
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("OK")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(icon, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        if(isfinish) {
                            ac.finish();
                        }
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        if(isfinish) {
                            ac.finish();
                        }
                    }
                })
                .build();
    }
}
