package com.itcraftsolution.picturepoint.CustomDialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itcraftsolution.picturepoint.R;

public class permission_Dailog extends Dialog {
    public permission_Dailog(@NonNull Context context) {
        super(context);
    }

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_dailog);

        btn = findViewById(R.id.btnPermission);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                SharedPreferences spf = getContext().getSharedPreferences("Permission" , Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = spf.edit();
                edit.putBoolean("Allow" , true);
                edit.apply();
                Toast.makeText(getContext(), "allow", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
