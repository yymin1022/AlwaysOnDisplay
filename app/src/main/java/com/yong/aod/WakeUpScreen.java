package com.yong.aod;

import android.content.*;
import android.os.*;

public class WakeUpScreen
{
	static SharedPreferences prefs;
	static SharedPreferences.Editor ed;
	
	private static PowerManager.WakeLock sCpuWakeLock;   
    
    static void acquireCpuLock(Context context)
	{
		prefs = context.getSharedPreferences("androesPrefName", context.MODE_PRIVATE);
		ed = prefs.edit();
		if (sCpuWakeLock != null) {           
            return;       
        }        
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);        
		if(prefs.getInt("proximity", 0) == 1){
			sCpuWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "PowerManager : ");
		}else{
			sCpuWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "PowerManager : ");
		}
        sCpuWakeLock.acquire();       
    }

    static void releaseCpuLock()
	{       
        if (sCpuWakeLock != null) {           
            sCpuWakeLock.release();           
            sCpuWakeLock = null;       
        }   
    }
}

