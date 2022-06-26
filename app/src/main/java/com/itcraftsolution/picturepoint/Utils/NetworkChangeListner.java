package com.itcraftsolution.picturepoint.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.itcraftsolution.picturepoint.R;

public class NetworkChangeListner extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Common.isNetworkConnected(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_Dialog = LayoutInflater.from(context).inflate(R.layout.internet_dailog, null);
            builder.setView(layout_Dialog);

            AppCompatButton btnRetry = layout_Dialog.findViewById(R.id.btnRetry);

            AlertDialog dailog = builder.create();
            dailog.setCancelable(false);
            dailog.show();

            dailog.getWindow().setGravity(Gravity.CENTER);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailog.dismiss();
                    onReceive(context, intent);
                }
            });
        }
    }
}
