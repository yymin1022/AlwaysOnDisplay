package com.yong.aod;

import android.os.*;
import android.widget.*;
import android.content.*;
import android.view.*;

import androidx.appcompat.app.AppCompatActivity;

public class ThemeActivity extends AppCompatActivity
{
	SharedPreferences prefs;
	SharedPreferences.Editor ed;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme);

		prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		ed = prefs.edit();

		RadioGroup radioGroup = findViewById(R.id.style);
		RadioGroup wallRadioGroup = findViewById(R.id.wallpaper);
		switch(prefs.getInt("setting",1)){
			case 1:
				radioGroup.check(R.id.g5clock);
				break;
			case 2:
				radioGroup.check(R.id.s7clock);
				break;
			case 3:
				radioGroup.check(R.id.s7calendar);
				break;
			case 4:
				radioGroup.check(R.id.analog);
				break;
			case 5:
				radioGroup.check(R.id.s8clock);
				break;
			case 6:
				radioGroup.check(R.id.s8VerticalClock);
				break;
			case 7:
				radioGroup.check(R.id.oneui);
				break;

		}
		switch(prefs.getInt("wallpaper",0)){
			case 0:
				wallRadioGroup.check(R.id.wallNone);
				break;
			case 1:
				wallRadioGroup.check(R.id.wall1);
				break;
			case 2:
				wallRadioGroup.check(R.id.wall2);
				break;
			case 3:
				wallRadioGroup.check(R.id.wall3);
				break;
			case 4:
				wallRadioGroup.check(R.id.wall4);
				break;
			case 5:
				wallRadioGroup.check(R.id.wall5);
		}
	}
	
	public void wallNone(View v){
		ed.remove("wallpaper");
		ed.putInt("wallpaper", 0);
		ed.apply();
	}

	public void wall1(View v){
		ed.remove("wallpaper");
		ed.putInt("wallpaper", 1);
		ed.apply();
	}

	public void wall2(View v){
		ed.remove("wallpaper");
		ed.putInt("wallpaper", 2);
		ed.apply();
	}

	public void wall3(View v){
		ed.remove("wallpaper");
		ed.putInt("wallpaper", 3);
		ed.apply();
	}

	public void wall4(View v){
		ed.remove("wallpaper");
		ed.putInt("wallpaper", 4);
		ed.apply();
	}

	public void wall5(View v){
		ed.remove("wallpaper");
		ed.putInt("wallpaper", 5);
		ed.apply();
	}

	public void g5clock(View v){
		ed.remove("setting");
		ed.putInt("setting", 1);
		ed.apply();
	}

	public void s7clock(View v){
		ed.remove("setting");
		ed.putInt("setting", 2);
		ed.apply();
	}

	public void analog(View v){
		ed.remove("setting");
		ed.putInt("setting", 4);
		ed.apply();
	}

	public void s7calendar(View v){
		ed.remove("setting");
		ed.putInt("setting", 3);
		ed.apply();
	}

	public void s8clock(View v){
		ed.remove("setting");
		ed.putInt("setting", 5);
		ed.apply();
	}

	public void s8VerticalClock(View v){
		ed.remove("setting");
		ed.putInt("setting", 6);
		ed.apply();
	}

	public void oneui(View v){
		ed.remove("setting");
		ed.putInt("setting", 7);
		ed.apply();
	}
}
