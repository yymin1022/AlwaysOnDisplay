package com.yong.aod;

import android.app.*;
import android.content.*;
import android.net.Uri;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.*;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		final SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		final SharedPreferences.Editor ed = prefs.edit();
		final SeekBar seek_brightness = findViewById(R.id.seek_brightness);
		final TextView currentBrightness = findViewById(R.id.currentBrightness);
		
		CheckBox check_burnin = findViewById(R.id.check_burnin);
		CheckBox check_DT2W =  findViewById(R.id.check_dt2w);
		CheckBox check_24h = findViewById(R.id.check_24h);
		CheckBox check_autoBrightness = findViewById(R.id.check_autoBrightness);
		CheckBox check_homeButton = findViewById(R.id.check_homeButton);
		CheckBox check_volumeButton = findViewById(R.id.check_volumeButton);
		CheckBox check_rotate = findViewById(R.id.check_rotate);
		CheckBox check_proximity = findViewById(R.id.check_proximity);
		
		if(Build.VERSION.SDK_INT<20){
			check_proximity.setEnabled(false);
			check_proximity.setText(getResources().getString(R.string.setting_proximity_not_support));
			ed.remove("proximity");
			ed.putInt("proximity", 0);
			ed.apply();
		}
		
		check_burnin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked){
					ed.remove("burnin");
					ed.putInt("burnin", 0);
					ed.apply();
				}else{
					ed.remove("burnin");
					ed.putInt("burnin", 1);
					ed.apply();
				}
			}
		});
			
		check_DT2W.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked){
					ed.remove("dt2w");
					ed.putInt("dt2w", 0);
					ed.apply();
				}else{
					ed.remove("dt2w");
					ed.putInt("dt2w", 1);
					ed.apply();
				}
			}
		});
		
		check_24h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked){
					ed.remove("timeFormat");
					ed.putInt("timeFormat", 0);
					ed.apply();
				}else{
					ed.remove("timeFormat");
					ed.putInt("timeFormat", 1);
					ed.apply();
				}
			}
		});
		
		check_autoBrightness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked){
					ed.remove("autoBrightness");
					ed.putInt("autoBrightness", 0);
					ed.apply();
					seek_brightness.setEnabled(true);
				}else{
					ed.remove("autoBrightness");
					ed.putInt("autoBrightness", 1);
					ed.apply();
					seek_brightness.setEnabled(false);
				}
			}
		});
		
		check_homeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked){
					ed.remove("home_button");
					ed.putInt("home_button", 0);
					ed.apply();
				}else{
					ed.remove("home_button");
					ed.putInt("home_button", 1);
					ed.apply();
				}
			}
		});
		
		check_volumeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked){
					ed.remove("volume_button");
					ed.putInt("volume_button", 0);
					ed.apply();
				}else{
					ed.remove("volume_button");			
					ed.putInt("volume_button", 1);
					ed.apply();
				}
			}
		});
		/*
		check_rotate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(!isChecked){
						ed.remove("rotate");
						ed.putInt("rotate", 0);
						ed.apply();
					}else{
						ed.remove("rotate");
						ed.putInt("rotate", 1);
						ed.apply();
					}
				}
		});
		*/
		check_proximity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(!isChecked){
						ed.remove("proximity");
						ed.putInt("proximity", 0);
						ed.apply();
					}else{
						ed.remove("proximity");
						ed.putInt("proximity", 1);
						ed.apply();
					}
				}
			});
		
		switch(prefs.getInt("burnin",1)){
			case 0:
				check_burnin.setChecked(false);
				break;
			case 1:
				check_burnin.setChecked(true);
				break;
		}
		
		switch(prefs.getInt("dt2w",1)){
			case 0:
				check_DT2W.setChecked(false);
				break;
			case 1:
				check_DT2W.setChecked(true);
				break;
		}
		
		switch(prefs.getInt("home_button",1)){
			case 0:
				check_homeButton.setChecked(false);
				break;
			case 1:
				check_homeButton.setChecked(true);
				break;
		}
		
		switch(prefs.getInt("timeFormat",1)){
			case 0:
				check_24h.setChecked(false);
				break;
			case 1:
				check_24h.setChecked(true);
				break;
		}
		
		switch(prefs.getInt("volume_button",1)){
			case 0:
				check_volumeButton.setChecked(false);
				break;
			case 1:
				check_volumeButton.setChecked(true);
				break;
		}
		
		switch(prefs.getInt("rotate",1)){
			case 0:
				check_rotate.setChecked(false);
				break;
			case 1:
				check_rotate.setChecked(true);
				break;
		}
		
		switch(prefs.getInt("proximity",1)){
			case 0:
				check_proximity.setChecked(false);
				break;
			case 1:
				check_proximity.setChecked(true);
				break;
		}
		
		switch(prefs.getInt("autoBrightness",0)){
			case 0:
				check_autoBrightness.setChecked(false);
				seek_brightness.setEnabled(true);
				seek_brightness.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
					public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
						currentBrightness.setText(progress + "%");
					}
					
					public void onStartTrackingTouch(SeekBar seekbar) {
					}

					public void onStopTrackingTouch(SeekBar seekbar) {
						int progress = seek_brightness.getProgress();
						if(progress == 0){
							progress = 1;
						}
						ed.remove("brightness");
						ed.apply();
						ed.putInt("brightness", progress);
						ed.apply();
					}});
				break;
			case 1:
				check_autoBrightness.setChecked(true);
				seek_brightness.setEnabled(false);
				break;
		}
		
		currentBrightness.setText(String.valueOf((prefs.getInt("brightness", 20))));
		seek_brightness.setProgress(prefs.getInt("brightness", 20));
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(isServiceRunning()){
			stopService(new Intent(this, MainService.class));
			startService(new Intent(this, MainService.class));
		}
	}
	
	public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.yong.aod.AODService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
	}
	
	public void themeActivity(View v){
		startActivity(new Intent(this, ThemeActivity.class));
	}


	public void removeNoti(View V){
		new AlertDialog.Builder(this)
			.setMessage(getResources().getString(R.string.setting_dialog_removenoti))
			.setCancelable(false)
			.setPositiveButton(getResources().getString(R.string.main_dialog_ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					intent.setData(Uri.parse("package:" + getPackageName()));
					startActivity(intent);
				}
				})
			.setNegativeButton("아니요",null)
			.show();
	}
}
