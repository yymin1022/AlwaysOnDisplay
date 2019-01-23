package com.yong.aod;

import android.content.*;
import android.widget.*;

public class BootReceiver extends BroadcastReceiver
{
	@Override
    public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
			SharedPreferences prefs = context.getSharedPreferences("androesPrefName", Context.MODE_PRIVATE);
			switch(prefs.getInt("service",2)){
				case 1:
					context.startService(new Intent(context, MainService.class));
					break;
				default:
					break;
			}
		}
	}
}
