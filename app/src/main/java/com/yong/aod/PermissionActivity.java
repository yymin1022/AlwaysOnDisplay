package com.yong.aod;
import android.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import com.gun0912.tedpermission.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class PermissionActivity extends AppCompatActivity
{
	int no_again = 0;
	boolean notiAllowed = false;
	boolean phonePermissionGranted = false;
	boolean notiError = false;
	boolean supportNotiService = true;
	
	Button notiButton;
	Button endButton;
	Button phoneStateButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_permission);
		notiButton = findViewById(R.id.btn_noti);
		endButton = findViewById(R.id.btn_end);
		phoneStateButton = findViewById(R.id.btn_phoneState);
		if(Build.VERSION.SDK_INT<19){
			supportNotiService = false;
			notiButton.setEnabled(false);
			notiButton.setText(getResources().getString(R.string.permission_noti_not_support));
		}
	}
	
	public void phoneState(View v){
		PermissionListener permissionlistener = new PermissionListener() {
			@Override
			public void onPermissionGranted() {}

			@Override
			public void onPermissionDenied(List<String> deniedPermissions) {}
		};
		TedPermission.with(this)
			.setPermissionListener(permissionlistener)
			.setDeniedMessage(getResources().getString(R.string.permission_warning))
			.setPermissions(Manifest.permission.READ_PHONE_STATE)
			.check();
		}
		
		public void notiPermission(View v){
			try{
				Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
				startActivity(intent);
			}catch(IllegalStateException e){
				Button notiButton = findViewById(R.id.btn_noti);
				notiError = true;
				notiButton.setEnabled(false);
				notiAllowed = true;
				notiButton.setText(getResources().getString(R.string.permission_allowed));
			}
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
			if(Build.VERSION.SDK_INT>=19){
				if(!notiError){
					if(isNotiPermissionAllowed()){
						notiButton.setEnabled(false);
						notiAllowed = true;
						notiButton.setText(getResources().getString(R.string.permission_noti) + " : " + getResources().getString(R.string.permission_allowed));
					}else{
						notiButton.setEnabled(true);
					}
				}
			}else{
				notiAllowed = true;
			}
			
			int Permission_PhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
			if(Permission_PhoneState== PackageManager.PERMISSION_DENIED){
				phoneStateButton.setEnabled(true);
			}else{
				phoneStateButton.setEnabled(false);
				phonePermissionGranted = true;
				phoneStateButton.setText(getResources().getString(R.string.permission_call) + " : " + getResources().getString(R.string.permission_allowed));
			}
			new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						if(notiAllowed&&phonePermissionGranted){
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
}
