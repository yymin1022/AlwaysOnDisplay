package com.yong.aod;

import android.app.*;
import android.content.*;
import android.graphics.BitmapFactory;
import android.os.*;
import android.util.*;
import android.provider.*;

import androidx.core.app.NotificationCompat;

public class AODService extends Service
{
	int originalCapacitiveButtonsState = 0;
	int originalTimeoutState = 0;
	
	static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

	NotificationCompat.Builder notificationBuilder;

	private BroadcastReceiver homeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
					if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
						Log.d("home", "pressed");
						context.sendBroadcast(new Intent("exit"));
					}
                }
            }

        }
    };
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeReceiver,filter);
		try{
			originalCapacitiveButtonsState = Settings.System.getInt(getContentResolver(), "button_key_light", 1500);
		}catch(Exception e){
			Log.d("Exception", e.toString());
		}
		try{
			originalTimeoutState = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 0);
		}catch(Exception e){
			Log.d("Exception", e.toString());
		}
		try{
			Settings.System.putInt(getContentResolver(), "button_key_light", 0);
		}catch(Exception e){
			Log.d("Exception", e.toString());
		}
		try{
			Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 5000);
		}catch(Exception e){
			Log.d("Exception", e.toString());
		}
		WakeUpScreen.acquireCpuLock(getApplicationContext());

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationChannel channel = new NotificationChannel("AlwaysOnDisplay", "Always On Display is Running", NotificationManager.IMPORTANCE_MIN);
			channel.setImportance(NotificationManager.IMPORTANCE_MIN);
			channel.setDescription("Always On Display is Running");
			if(notificationManager != null){
				notificationManager.createNotificationChannel(channel);
			}
		}
		notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "AlwaysOnDisplay")
				.setSmallIcon(R.drawable.ic_launcher)
				.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher))
				.setContentTitle("Always On Display")
				.setContentText("Always On Display")
				.setOngoing(true)
				.setPriority(Notification.PRIORITY_MIN)
				.setAutoCancel(false);
		startForeground(1379, notificationBuilder.build());
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		try{
			unregisterReceiver(homeReceiver);
		}catch(IllegalArgumentException e){
			Log.d("Exception", e.toString());
		}
		try{
			Settings.System.putInt(getContentResolver(), "button_key_light", originalCapacitiveButtonsState);
		}catch(RuntimeException e){
			Log.d("Exception", e.toString());
		}
		try{
			Settings.System.putLong(getContentResolver(), "button_key_light", originalCapacitiveButtonsState);
		}catch(Exception e){
			Log.d("Exception", e.toString());
		}
		try{
			Settings.Secure.putInt(getContentResolver(), "button_key_light", originalCapacitiveButtonsState);
		}catch(Exception e){
			Log.d("Exception", e.toString());
		}
		try{
			Settings.System.putInt(getContentResolver(), "button_key_light", originalCapacitiveButtonsState);
		}catch(Exception e){
			Log.d("Exception", e.toString());
		}
		try{
			Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, originalTimeoutState);
		} catch (Exception e){
			Log.d("Exception", e.toString());
		}
		WakeUpScreen.releaseCpuLock();
		stopForeground(true);
	}
	
	@Override
	public IBinder onBind(Intent p1)
	{
		return null;
	}

}
