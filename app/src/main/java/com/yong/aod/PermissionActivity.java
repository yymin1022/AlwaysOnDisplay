package com.yong.aod;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;
import java.util.Set;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class PermissionActivity extends AppCompatActivity
{
    boolean notiAllowed = false;
	boolean phonePermissionGranted = false;
	boolean writeSettingsGranted = false;
	
	Button notiButton;
	Button endButton;
	Button phoneStateButton;
	Button writeSettingsButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_permission);
		notiButton = findViewById(R.id.btn_noti);
		endButton = findViewById(R.id.btn_end);
		phoneStateButton = findViewById(R.id.btn_phoneState);
		writeSettingsButton = findViewById(R.id.btn_write_setting);
	}
	
	public void phoneState(View v){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.permission_call))
                .setMessage(getResources().getString(R.string.permission_call_info))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.main_dialog_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionListener permissionlistener = new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted() {}

                                    @Override
                                    public void onPermissionDenied(List<String> deniedPermissions) {}
                                };
                                TedPermission.with(PermissionActivity.this)
                                        .setPermissionListener(permissionlistener)
                                        .setDeniedMessage(getResources().getString(R.string.permission_warning))
                                        .setPermissions(Manifest.permission.READ_PHONE_STATE)
                                        .check();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("아니요",null)
                .show();
	}

	@RequiresApi(Build.VERSION_CODES.KITKAT)
    public void notiReceive(View v) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.permission_noti))
                .setMessage(getResources().getString(R.string.permission_noti_info))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.main_dialog_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                                startActivity(intent);
                                dialog.cancel();
                            }
                    })
                .setNegativeButton("아니요",null)
                .show();
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public void writeSetting(View v){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.permission_write_settings))
                .setMessage(getResources().getString(R.string.permission_write_settings_info))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.main_dialog_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                intent.setData(Uri.parse("package:" + PermissionActivity.this.getPackageName()));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("아니요",null)
                .show();
    }

    public void end(View v){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.remove("isPermissionGranted");
        ed.apply();
        ed.putBoolean("isPermissionGranted", true);
        ed.apply();
        finish();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        endButton.setEnabled(false);

        //If higher that API 19 Kitkat, ask for Notification Read Permission.
        if(Build.VERSION.SDK_INT>=19){
            if(isNotiPermissionAllowed()){
                notiButton.setEnabled(false);
                notiAllowed = true;
                notiButton.setText(String.format(getResources().getString(R.string.permission_allowed), getResources().getString(R.string.permission_noti)));
            }else{
                notiButton.setEnabled(true);
                notiButton.setText(String.format(getResources().getString(R.string.permission_not_allowed), getResources().getString(R.string.permission_noti)));
            }
        }else{
            notiButton.setEnabled(false);
            notiAllowed = true;
            notiButton.setText(String.format(getResources().getString(R.string.permission_not_support), getResources().getString(R.string.permission_noti)));
        }

        //Check if PhoneState permission allowed. If not, ask for Permission grant.
        int Permission_PhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if(Permission_PhoneState == PackageManager.PERMISSION_DENIED){
            phoneStateButton.setEnabled(true);
            phoneStateButton.setText(String.format(getResources().getString(R.string.permission_not_allowed), getResources().getString(R.string.permission_call)));
        }else{
            phoneStateButton.setEnabled(false);
            phonePermissionGranted = true;
            phoneStateButton.setText(String.format(getResources().getString(R.string.permission_allowed), getResources().getString(R.string.permission_call)));
        }

        //Check if Write Settings permission allowed. If not, open Grant Activity.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (Settings.System.canWrite(this)){
                writeSettingsGranted = true;
                writeSettingsButton.setEnabled(false);
                writeSettingsButton.setText(String.format(getResources().getString(R.string.permission_allowed), getResources().getString(R.string.permission_write_settings)));
            }else{
                writeSettingsButton.setEnabled(true);
                writeSettingsButton.setText(String.format(getResources().getString(R.string.permission_not_allowed), getResources().getString(R.string.permission_write_settings)));
            }
        }else{
            writeSettingsGranted = true;
            writeSettingsButton.setEnabled(false);
            writeSettingsButton.setText(String.format(getResources().getString(R.string.permission_allowed), getResources().getString(R.string.permission_write_settings)));
        }

        //If all permissions are granted, enable complete button.
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(notiAllowed  && phonePermissionGranted && writeSettingsGranted){
                    Button endButton = findViewById(R.id.btn_end);
                    endButton.setEnabled(true);
                    endButton.setText(getResources().getString(R.string.permission_finish));
                }
            }
        }, 500);

    }

	private boolean isNotiPermissionAllowed() {
		Set<String> notiListenerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
		String myPackageName = getPackageName();

		for(String packageName : notiListenerSet) {
			if(packageName == null) {
				continue;
			}
			if(packageName.equals(myPackageName)) {
				return true;
			}
		}
		return false;
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK);
    }
}
