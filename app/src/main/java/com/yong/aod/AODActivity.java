package com.yong.aod;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.provider.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import com.ssomai.android.scalablelayout.*;
import java.text.*;
import java.util.*;

import androidx.appcompat.widget.AppCompatImageView;

public class AODActivity extends Activity 
{
    boolean disableVolume = false;
    boolean isCharging = false;
    boolean use24h = false;
    boolean useAutoBrightness = false;
    boolean useBurnIn = false;
	boolean useDT2W = false;
	boolean useS8Home = false;

    String battery = "";
	String NEW_NOTIFICATION = "new_notification";

    int batteryRatio = 0;
    int burninMarginMaxheight = 0;
    int clockType = 1;
    int currentApiVersion = 0;
	int defaultAutoBrightnessValue = 0;
	int heightMargin = 0;
    int randHeight = 0;
    int status = 0;
	int widthMargin = 0;

	DisplayMetrics metrics;
	FrameLayout.LayoutParams plControl;
	ScalableLayout clockLayout;
    SharedPreferences prefs;
    SharedPreferences.Editor ed;
	WindowManager windowManager;
	
	private WindowManager.LayoutParams moldLp;
	private WindowManager.LayoutParams mnewLp;
	private Window mWindow;
	
	public Context mContext;
	
	//Notification Blinker Receiver
	private BroadcastReceiver newNotificationBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            View notiLed = findViewById(R.id.icons_wrapper);
			notiLed.setVisibility(View.VISIBLE);
			Animation mAnimation = new AlphaAnimation(1, 0);
			mAnimation.setDuration(500);
			mAnimation.setInterpolator(new LinearInterpolator());
			mAnimation.setRepeatCount(Animation.INFINITE);
			mAnimation.setRepeatMode(Animation.REVERSE);
			notiLed.startAnimation(mAnimation);
        }
    };
	
	//Finish AOD from another class using this Receiver
	private final BroadcastReceiver exitReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("AOD_LOG", "exitReceiver");
			exitAOD();
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.aod_black);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		mContext = this;
		prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		ed = prefs.edit();
		ed.apply();
		initializeAOD();
	}

	//Battery Handler
	Handler mBatteryHandler = new Handler() {
		public void handleMessage(Message msg) {
			IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
			status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			batteryRatio = getBatteryPercentage(getApplicationContext());
			isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
			TextView tv  = findViewById(R.id.battery);
			ImageView IcBattery = findViewById(R.id.ic_battery);
			if (isCharging == true){
				battery=String.valueOf(batteryRatio);
				tv.setText(battery+"%");
				if(batteryRatio<=15){
					IcBattery.setImageResource(R.drawable.battery_charge_1);
				}else if(batteryRatio<=45){
					IcBattery.setImageResource(R.drawable.battery_charge_2);
				}else if(batteryRatio<=60){
					IcBattery.setImageResource(R.drawable.battery_charge_3);
				}else if(batteryRatio<=75){
					IcBattery.setImageResource(R.drawable.battery_charge_4);
				}else if(batteryRatio<=99){
					IcBattery.setImageResource(R.drawable.battery_charge_5);
				}else if(batteryRatio<=100){
					IcBattery.setImageResource(R.drawable.battery_charge_6);
				}
			}else{
				battery=String.valueOf(batteryRatio);
				tv.setText(battery+"%");
				if(batteryRatio<=15){
					IcBattery.setImageResource(R.drawable.battery_15);
				}else if(batteryRatio<=45){
					IcBattery.setImageResource(R.drawable.battery_45);
				}else if(batteryRatio<=60){
					IcBattery.setImageResource(R.drawable.battery_60);
				}else if(batteryRatio<=75){
					IcBattery.setImageResource(R.drawable.battery_75);
				}else if(batteryRatio<=95){
					IcBattery.setImageResource(R.drawable.battery_95);
				}else if(batteryRatio<=100){
					IcBattery.setImageResource(R.drawable.battery_100);
				}
			}
			mBatteryHandler.sendEmptyMessageDelayed(0, 3000);
		}
	};
	
	//Burn In Protection Handler
	Handler mBurnInHandler = new Handler() {
		public void handleMessage(Message msg) {
			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager windowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
			windowManager.getDefaultDisplay().getMetrics(metrics);
			ScalableLayout clockLayout = findViewById(R.id.clock);
			FrameLayout.LayoutParams plControl = (FrameLayout.LayoutParams) clockLayout.getLayoutParams();
			if(useBurnIn == true){
				if(clockType == 3 || clockType == 6){
					burninMarginMaxheight = (metrics.heightPixels/5);
					randHeight = getRandomMath(burninMarginMaxheight, 1);
					plControl.bottomMargin = randHeight;
					clockLayout.setLayoutParams(plControl);
				}else{
					windowManager.getDefaultDisplay().getMetrics(metrics);
					burninMarginMaxheight = (metrics.heightPixels/4);
					randHeight = getRandomMath(burninMarginMaxheight, 1);
					plControl.bottomMargin = randHeight;
					clockLayout.setLayoutParams(plControl);
				}
				mBurnInHandler.sendEmptyMessageDelayed(0, 15000);
			}
		}
	};
		
	//Clock Handler
	Handler mClockHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(clockType){
				case 3:
					try{
						TextView ClockText = findViewById(R.id.time);
						String ClockHour = new String("HH");
						String ClockMin = new String("mm");
						SimpleDateFormat Clock = new SimpleDateFormat(ClockHour, Locale.getDefault());
						SimpleDateFormat Min = new SimpleDateFormat(ClockMin, Locale.getDefault());
						ClockText.setText(Clock.format(new Date()) + ":" + Min.format(new Date()));
					}catch(Exception e){}
					break;
				case 4:
					try{
						TextView DateText = findViewById(R.id.date);
						String DateFormat = new String("MM"+getString(R.string.aod_date_month) +" dd"+getString(R.string.aod_date_date) +" E"+getString(R.string.aod_date_day));
						SimpleDateFormat Date = new SimpleDateFormat(DateFormat, Locale.getDefault());
						DateText.setText(Date.format(new Date()));
					}catch(Exception e){}
					break;
				case 6:
					try{
						TextView DateText = findViewById(R.id.date);
						TextView ClockText = findViewById(R.id.time);
						String DateFormat = new String("MM"+getString(R.string.aod_date_month) +" dd"+getString(R.string.aod_date_date) +" E"+getString(R.string.aod_date_day));
						SimpleDateFormat Date = new SimpleDateFormat(DateFormat, Locale.getDefault());
						DateText.setText(Date.format(new Date()));
						String ClockHour = new String("HH");
						String ClockMin = new String("mm");
						SimpleDateFormat Clock = new SimpleDateFormat(ClockHour, Locale.getDefault());
						SimpleDateFormat Min = new SimpleDateFormat(ClockMin, Locale.getDefault());
						if(prefs.getInt("timeFormat",1)==0){
							if(Integer.parseInt(Clock.format(new Date()))>12){
								int hour = Integer.parseInt(Clock.format(new Date()));
								hour = hour-12;
								ClockText.setText(String.format("%02d", hour) + "\n" + Min.format(new Date()));
							}else{
								int hour = Integer.parseInt(Clock.format(new Date()));
								ClockText.setText(String.format("%02d", hour) + "\n" + Min.format(new Date()));
							}
						}else{
							ClockText.setText(Clock.format(new Date()) + "\n" + Min.format(new Date()));
						}
					}catch(Exception e){}
					break;
				default:
					try{
						TextView DateText = findViewById(R.id.date);
						TextView ClockText = findViewById(R.id.time);
						String DateFormat = new String("MM"+getString(R.string.aod_date_month) +" dd"+getString(R.string.aod_date_date) +" E"+getString(R.string.aod_date_day));
						SimpleDateFormat Date = new SimpleDateFormat(DateFormat, Locale.getDefault());
						DateText.setText(Date.format(new Date()));
						String ClockHour = new String("HH");
						String ClockMin = new String("mm");
						SimpleDateFormat Clock = new SimpleDateFormat(ClockHour, Locale.getDefault());
						SimpleDateFormat Min = new SimpleDateFormat(ClockMin, Locale.getDefault());
						if(prefs.getInt("timeFormat",1)==0){
							if(Integer.parseInt(Clock.format(new Date()))>12){
								int hour = Integer.parseInt(Clock.format(new Date()));
								hour = hour-12;
								ClockText.setText(String.valueOf(hour) + ":" + Min.format(new Date()));
							}else{
								int hour = Integer.parseInt(Clock.format(new Date()));
								ClockText.setText(String.valueOf(hour) + ":" + Min.format(new Date()));
							}
						}else{
							ClockText.setText(Clock.format(new Date()) + ":" + Min.format(new Date()));
						}
					}catch(Exception e){}
					break;
			}
			mClockHandler.sendEmptyMessageDelayed(0, 1000);
		}
	};
	
	//Block HardWare Buttons
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean keyBoolean = false;
    	switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
			case KeyEvent.KEYCODE_MENU:
				keyBoolean = true;
				break;
			case KeyEvent.KEYCODE_VOLUME_UP:
			case KeyEvent.KEYCODE_VOLUME_DOWN:
			case KeyEvent.KEYCODE_MEDIA_NEXT:
			case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
				keyBoolean = disableVolume;
				break;
			case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
				if(disableVolume){
					keyBoolean = true;
				}else{
					keyBoolean = false;
					AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
					if (am != null && !am.isMusicActive())
						((AppCompatImageView) findViewById(R.id.play)).setImageResource(R.drawable.ic_pause);
					else
						((AppCompatImageView) findViewById(R.id.play)).setImageResource(R.drawable.ic_play);
				}
				break;
		}
		return keyBoolean;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
		{
			getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}
	
	public void initializeAOD(){
		Log.v("AOD_LOG", "initializeAOD");
		
		//Set Preference to AOD Enabled
		ed.remove("AOD");
		ed.apply();
		ed.putBoolean("AOD", true);
		ed.apply();
		
		//Set Clock Font and Clock Theme
		TextView time;
		Typeface font;
		clockType = prefs.getInt("setting", 1);
		switch(clockType){
			case 1:
				setContentView(R.layout.aod_g5);
				time = findViewById(R.id.time);
				font = Typeface.createFromAsset( getAssets(), "fonts/lg.ttf" );
				time.setTypeface(font);
				break;
			case 2:
				setContentView(R.layout.aod_s7);
				time = findViewById(R.id.time);
				font = Typeface.createFromAsset( getAssets(), "fonts/samsung.ttf" );
				time.setTypeface(font);
				break;
			case 3:
				setContentView(R.layout.aod_cal);
				time = findViewById(R.id.time);
				font = Typeface.createFromAsset( getAssets(), "fonts/samsung.ttf" );
				time.setTypeface(font);
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				long endOfMonth = calendar.getTimeInMillis();
				calendar = Calendar.getInstance();
				calendar.set(Calendar.DATE, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				long startOfMonth = calendar.getTimeInMillis();
				CalendarView mCalendarView = findViewById(R.id.aodCalendar);
				mCalendarView.setMaxDate(endOfMonth);
				mCalendarView.setMinDate(startOfMonth);
				mCalendarView.setEnabled(false);
				break;
			case 4:
				setContentView(R.layout.aod_anal);
				break;
			case 5:
				setContentView(R.layout.aod_s8);
				time = findViewById(R.id.time);
				font = Typeface.createFromAsset( getAssets(), "fonts/samsung_s8.ttf" );
				time.setTypeface(font);
				break;
			case 6:
				setContentView(R.layout.aod_s8v);
				time = findViewById(R.id.time);
				font = Typeface.createFromAsset( getAssets(), "fonts/samsung_s8.ttf" );
				time.setTypeface(font);
				break;
			case 7:
				setContentView(R.layout.aod_oneui);
				break;
		}
		
		//Get User Settings
		if(prefs.getInt("dt2w",1) == 1){
			useDT2W = true;
		}
		if(prefs.getInt("rotate",1) == 1){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		if(prefs.getInt("home_button",1) == 1){
			useS8Home = true;
		}
		if(prefs.getInt("burnin",1) == 1){
			useBurnIn = true;
		}
		if(prefs.getInt("volume_button",1) == 1){
			disableVolume = true;
		}
		if(prefs.getInt("timeFormat",1) == 1){
			use24h = true;
		}
		if(prefs.getInt("autoBrightness",1) == 0){
			useAutoBrightness = true;
		}
		
		//Start AOD Background Management Service
		startService(new Intent(this, AODService.class));
		
		//Register Exit Receiver
		registerReceiver(exitReceiver, new IntentFilter("exit"));
		
		//Register Notification Receiver
		registerReceiver(newNotificationBroadcast, new IntentFilter(NEW_NOTIFICATION));
		
		//Start Battery, Clock, BurnIn Handler
		mBatteryHandler.sendEmptyMessage(0);
		mClockHandler.sendEmptyMessage(0);
		mBurnInHandler.sendEmptyMessage(0);

		metrics = new DisplayMetrics();
		windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		if (windowManager != null){
			windowManager.getDefaultDisplay().getMetrics(metrics);
		}

		if(clockType == 3 || clockType == 6){
			heightMargin = (metrics.heightPixels/7);
			widthMargin = (metrics.widthPixels/4);
		}else{
			heightMargin = (metrics.heightPixels/7);
			widthMargin = (metrics.widthPixels/100)*18;
		}
		clockLayout = findViewById(R.id.clock);
		plControl = (FrameLayout.LayoutParams) clockLayout.getLayoutParams();
		plControl.bottomMargin = heightMargin;
		plControl.leftMargin = widthMargin;
		clockLayout.setLayoutParams(plControl);

		mWindow = getWindow();
		moldLp = mWindow.getAttributes();
		mnewLp = mWindow.getAttributes();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		currentApiVersion = Build.VERSION.SDK_INT;
		
		//Set Wallpaper
		ImageView wallpaperView = findViewById(R.id.wallpaper);
		switch(prefs.getInt("wallpaper",0)){
			case 0:
				wallpaperView.setVisibility(View.INVISIBLE);
				wallpaperView.setImageDrawable(getResources().getDrawable(R.drawable.wallpaper_01));
				break;
			case 1:
				wallpaperView.setVisibility(View.VISIBLE);
				wallpaperView.setImageDrawable(getResources().getDrawable(R.drawable.wallpaper_01));
				break;
			case 2:
				wallpaperView.setVisibility(View.VISIBLE);
				wallpaperView.setImageDrawable(getResources().getDrawable(R.drawable.wallpaper_02));
				break;
			case 3:
				wallpaperView.setVisibility(View.VISIBLE);
				wallpaperView.setImageDrawable(getResources().getDrawable(R.drawable.wallpaper_03));
				break;
			case 4:
				wallpaperView.setVisibility(View.VISIBLE);
				wallpaperView.setImageDrawable(getResources().getDrawable(R.drawable.wallpaper_04));
				break;
			case 5:
				wallpaperView.setVisibility(View.VISIBLE);
				wallpaperView.setImageDrawable(getResources().getDrawable(R.drawable.wallpaper_05));
				break;
		}
		
		//Set DT2W Usage
		if(useDT2W){
			View knockButton = findViewById(R.id.knock);
			knockButton.setOnTouchListener(new OnTouchListener() {
					private GestureDetector gestureDetector = new GestureDetector(AODActivity.this, new GestureDetector.SimpleOnGestureListener() {
							@Override
							public boolean onDoubleTap(MotionEvent e) {
								sendBroadcast(new Intent("exit"));
								return super.onDoubleTap(e);
							}
						});

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						gestureDetector.onTouchEvent(event);
						v.performClick();
						return true;
					}
				});
		}
			
		//Set S8 Home Button Usage
		ImageView homeButton = findViewById(R.id.home_button);
		final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		if(useS8Home){
			homeButton.setVisibility(View.VISIBLE);
			homeButton.setOnTouchListener(new OnTouchListener() {
			private GestureDetector gestureDetector = new GestureDetector(AODActivity.this, new GestureDetector.SimpleOnGestureListener() {
					@Override
					public void onLongPress(MotionEvent e) {
						vibrator.vibrate(30); 
						sendBroadcast(new Intent("exit"));
						return ;
					}
					@Override
					public boolean onSingleTapUp(MotionEvent e){
						vibrator.vibrate(30);
						return true;
					}
					@Override
					public boolean onDown(MotionEvent e){
						vibrator.vibrate(30);
						return true;
					}
				});
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					gestureDetector.onTouchEvent(event);
					return true;
				}
			});
		}else{
			homeButton.setVisibility(View.INVISIBLE);
		}
		
		//Hide Soft Key
		final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
		{
			getWindow().getDecorView().setSystemUiVisibility(flags);
			final View decorView = getWindow().getDecorView();
			decorView
				.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener()
				{
					@Override
					public void onSystemUiVisibilityChange(int visibility)
					{
						if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
						{
							decorView.setSystemUiVisibility(flags);
						}
					}
				});
		}
		
		//Set Auto Brightness Adjustment Usage
		if(useAutoBrightness){
			try {
				defaultAutoBrightnessValue = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
			}catch(Settings.SettingNotFoundException e){
				Log.e("Exception", e.toString());
			}
			try {
				Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 1);
			} catch(RuntimeException e){
				Log.e("Exception", e.toString());
			}
		}else{
			mnewLp.screenBrightness = (prefs.getInt("brightness",20))/100f;
			mWindow.setAttributes(mnewLp);
		}
	}
	
	public void exitAOD(){
		Log.v("AOD_LOG", "exitAOD");
		
		//Set Preferences to AOD Disabled
		ed.remove("AOD");
		ed.apply();
		ed.putBoolean("AOD", false);
		ed.apply();
		
		//Unregister Receivers
		try{
			unregisterReceiver(exitReceiver);
		}catch(IllegalArgumentException e){
			Log.e("Exception", e.toString());
		}
		try{
			unregisterReceiver(newNotificationBroadcast);
		} catch(IllegalArgumentException e){
			Log.e("Exception", e.toString());
		}
		stopService(new Intent(AODActivity.this, AODService.class));
		
		//Stop Battery, Clock, BurnIn Handler
		mBatteryHandler.removeMessages(0);
		mClockHandler.removeMessages(0);
		mBurnInHandler.removeMessages(0);
		
		//Reset Notification Blinker
		View notiLed = findViewById(R.id.icons_wrapper);
		notiLed.setVisibility(View.GONE);
		notiLed.clearAnimation();
		
		//Reset Brightness to Default
		if(useAutoBrightness){
			if(defaultAutoBrightnessValue == 1){
				try {
					Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 1);
				} catch(RuntimeException e){
					Log.e("Exception", e.toString());
				}
			}else{
				try {
					Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 0);
				}catch(RuntimeException e){
					Log.e("Exception", e.toString());
				}
			}
		}else{
			mWindow.setAttributes(moldLp);
		}
	}
	
	public int getRandomMath(int max, int offset) {
		return (int)(Math.random() * max) + offset;
	}

	public static int getBatteryPercentage(Context context) {
		Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		if(batteryStatus != null){
			int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			float batteryPct = level / (float)scale;
			return (int)(batteryPct * 100);
		}else{
			return 0;
		}
	}
}
