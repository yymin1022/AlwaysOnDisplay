package com.yong.aod;

import android.app.*;
import android.content.*;
import android.os.*;
import android.graphics.*;
import android.telephony.*;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MainService extends Service
{
	SharedPreferences prefs;
	
	BroadcastReceiver mybroadcast = new BroadcastReceiver(){    
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
				if(!prefs.getBoolean("AOD", false)){
					Log.d("Receiver", "AOD Run");

					Intent aodIntent = new Intent(context, AODActivity.class);
					aodIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					aodIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					aodIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					startActivity(aodIntent);
				}else{
					Log.d("Receiver", "AOD Exit Screen On");

					PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
					PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP, "PowerManager : ");
					wakeLock.acquire(3000);
					
					sendBroadcast(new Intent("exit"));
				}
			}
		}
	};
	
	private PhoneStateListener phoneStateListener = new PhoneStateListener(){
		@Override
		public void onCallStateChanged(int state, String incomingNumber)
		{
			if(state == TelephonyManager.CALL_STATE_RINGING || state == TelephonyManager.CALL_STATE_OFFHOOK){
				try{
					unregisterReceiver(mybroadcast);
				}catch(IllegalArgumentException e){
					Log.e("Exception", e.toString());
				}
			}else if(state == TelephonyManager.CALL_STATE_IDLE){
				registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
			}
		}
	};
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		//Register Screen Off Recevier
		registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));

		//Register Call related Listener to stop Service when Calling
		TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		tManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "Always On Display")
			.setSmallIcon(R.drawable.ic_launcher)
			.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher))
			.setContentTitle("Always On Display")
			.setContentText("Always On Display" + getResources().getString(R.string.noti_text_running))
			.setOngoing(true)
			.setPriority(Notification.PRIORITY_MIN)
			.setAutoCancel(false);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationChannel channel = new NotificationChannel("Always On Display", "Always On Display is Running", NotificationManager.IMPORTANCE_MIN);
			channel.setDescription("Always On Display is Running");
			notificationManager.createNotificationChannel(channel);
		}
		startForeground(1111, notificationBuilder.build());
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		try{
			unregisterReceiver(mybroadcast);
		}catch(IllegalArgumentException e){
			Log.e("Exception", e.toString());
		}
		stopForeground(true);
	}
	
	@Override
	public IBinder onBind(Intent p1)
	{
		return null;
	}
}
