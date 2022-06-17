package com.itcraftsolution.picturepoint.CustomDialog;

import android.app.Activity;
import android.app.Dialog;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itcraftsolution.picturepoint.R;

import java.util.logging.LogRecord;

public class Custom_Dialog {

    private ProgressBar progressBar;
    private TextView progressText;
    int i = 0;

    public void showDialog(Activity activity)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        progressBar = activity.findViewById(R.id.progressBar);
        progressText = activity.findViewById(R.id.progress_text);

        Handler handler = new Handler();
        dialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(i <= 100)
                {
                    progressText.setText("" + i);
                    progressBar.setProgress(i);
                    i++;
                    handler.postDelayed(this,200);
                }else{
                    handler.removeCallbacks(this);
                    dialog.dismiss();
                    Toast.makeText(activity, "Set Wallpaper SuccessFully", Toast.LENGTH_SHORT).show();
                }
            }
        },200);



    }
}
