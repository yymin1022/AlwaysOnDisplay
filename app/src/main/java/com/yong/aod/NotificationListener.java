package com.yong.aod;

import android.annotation.TargetApi;
import android.service.notification.*;
import android.app.*;
import android.content.pm.*;
import android.graphics.drawable.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;

import androidx.core.content.ContextCompat;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class NotificationListener extends NotificationListenerService implements ContextConstatns {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		return super.onBind(intent);
	}

    @Override
    public void onNotificationPosted(StatusBarNotification added) {
        if (added.isClearable() && added.getNotification().priority >= Notification.PRIORITY_LOW) {
            String title = added.getNotification().extras.getString(Notification.EXTRA_TITLE) + " ";
            if (title.equals("null"))
                title = added.getNotification().extras.getString(Notification.EXTRA_TITLE_BIG) + " ";
            String content = added.getNotification().extras.getString(Notification.EXTRA_TEXT) + " ";
            if (content.equals("null") || content.isEmpty())
                content = added.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT_LINES) + " ";
            if (content.equals("null") || content.isEmpty())
                content = added.getNotification().extras.getCharSequence(Notification.EXTRA_SUMMARY_TEXT) + " ";
            Drawable icon = getIcon(added);
            ApplicationInfo notificationAppInfo = null;
            try {
                notificationAppInfo = getPackageManager().getApplicationInfo(added.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if(Globals.newNotification()!=null){
                Globals.notifications.get(Globals.newNotification()).setNew(false);
            }
            NotificationHolder newNotification = new NotificationHolder(title, content, icon, notificationAppInfo != null ? getPackageManager().getApplicationLabel(notificationAppInfo) : null, added.getNotification().contentIntent, true);
            Globals.notifications.put(getUniqueKey(added), newNotification);

			sendBroadcast(new Intent(NEW_NOTIFICATION));
			Log.d("NotiReceivedService", "True");
        }
        
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification removed) {
        Globals.notifications.remove(getUniqueKey(removed));
    }

    private String getUniqueKey(StatusBarNotification notification) {
        return notification.getPackageName();
    }


    private Drawable getIcon(StatusBarNotification notification) {
        Drawable returnValue = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            try{
				returnValue = notification.getNotification().getSmallIcon().loadDrawable(this);
			}catch(Exception e){
                Log.e("Exception", e.toString());
            }
        }else{
			try{
            	returnValue = ContextCompat.getDrawable(getApplicationContext(), notification.getNotification().icon);
			}catch(Exception e){
                Log.e("Exception", e.toString());
            }
		}
		return returnValue;
    }



    public static class NotificationHolder {
        private String appName;
        private Drawable icon;
        private String title, message;
        private PendingIntent intent;
        private boolean isNew;


        NotificationHolder(String title, String message, Drawable icon, CharSequence appName, PendingIntent intent, boolean newnotification) {
            this.icon = icon;
            this.title = title;
            this.message = message;
            this.appName = (String) appName;
            if (this.message.equals("null"))
                this.message = "";
            if (this.title.equals("null"))
                this.title = "";
            this.intent = intent;
            this.isNew = newnotification;
        }


        Drawable getIcon(Context context) {
            if (icon != null)
                icon.mutate().setColorFilter(ContextCompat.getColor(context, android.R.color.primary_text_dark), PorterDuff.Mode.MULTIPLY);
            return icon;
        }

        public PendingIntent getIntent() {
            return intent;
        }

        public boolean getNew(){return isNew;}

        public void setNew(boolean x){isNew = x;}

    }
}
