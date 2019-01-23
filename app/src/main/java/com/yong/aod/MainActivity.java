package com.yong.aod;

import android.app.*;
import android.content.*;
import android.os.*;
import android.provider.Settings;
import android.view.*;
import android.widget.*;
import com.fsn.cauly.*;
import android.net.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements CaulyCloseAdListener
{
	private CaulyAdView adView;
	private static final String APP_CODE = "TOeplGZT";
 	CaulyCloseAd mCloseAd ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		final SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		if(!prefs.getBoolean("isPermissionGranted", false)){
			startActivity(new Intent(this, PermissionActivity.class));
		}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                Toast.makeText(this, "onCreate: Already Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "onCreate: Not Granted. Permission Requested", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

		final Switch serviceToggle = findViewById(R.id.serviceSwitch);
		serviceToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton switchView, boolean isChecked)
			{
				if(isChecked){
					serviceToggle.setText(R.string.main_text_running);
					SharedPreferences.Editor ed = prefs.edit();
					ed.remove("service");
					ed.apply();
					ed.putInt("service", 1);
					ed.apply();
					ed.remove("AOD");
					ed.apply();
					ed.putBoolean("AOD", false);
					ed.apply();
					startService(new Intent(MainActivity.this, MainService.class));
				}else{
					serviceToggle.setText(R.string.main_text_not_running);
					SharedPreferences.Editor ed = prefs.edit();
					ed.remove("service");
					ed.apply();
					ed.putInt("service", 2);
					ed.apply();
					stopService(new Intent(MainActivity.this, MainService.class));
				}
			}
		});
		if(isServiceRunning()){
			serviceToggle.setChecked(true);
			serviceToggle.setText(R.string.main_text_running);
		}else{
			serviceToggle.setChecked(false);
			serviceToggle.setText(R.string.main_text_not_running);
		}
		if(!prefs.getBoolean("ad_removed",false)){
			showBanner();
		}else{
			LinearLayout layout=findViewById(R.id.mainLayout);
			layout.setVisibility(View.GONE);
		}
		CaulyAdInfo closeAdInfo = new CaulyAdInfoBuilder(APP_CODE).build();
		mCloseAd = new CaulyCloseAd();
		mCloseAd.setButtonText("취소", "종료");
		mCloseAd.setDescriptionText("종료하시겠습니까?");
		mCloseAd.setAdInfo(closeAdInfo);
		mCloseAd.setCloseAdListener(this); 
		mCloseAd.disableBackKey();
	}
	
	@Override
	protected void onResume() {
		super.onResume(); 
		if (mCloseAd != null)
 			mCloseAd.resume(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
			if(!prefs.getBoolean("ad_removed",false)){
				if (mCloseAd.isModuleLoaded())
				{
					mCloseAd.show(this);
				} 
				else
				{
					showDefaultClosePopup();
				}
			}else{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showDefaultClosePopup()
	{
		new AlertDialog.Builder(this).setTitle("").setMessage("종료 하시겠습니까?")
			.setPositiveButton("예", new DialogInterface.OnClickListener() {
 			    @Override
 			    public void onClick(DialogInterface dialog, int which) {
					finish();
 			    }
			})
			.setNegativeButton("아니요",null)
			.show();
 	}

	@Override
 	public void onFailedToReceiveCloseAd(CaulyCloseAd ad, int errCode,String errMsg) {
 	}
 	@Override
 	public void onLeaveCloseAd(CaulyCloseAd ad) {
 	}
 	@Override
 	public void onReceiveCloseAd(CaulyCloseAd ad, boolean isChargable) {

 	}	
 	@Override
 	public void onLeftClicked(CaulyCloseAd ad) {

 	}	
 	@Override
 	public void onRightClicked(CaulyCloseAd ad) {
 		finish();
 	}
 	@Override
 	public void onShowedCloseAd(CaulyCloseAd ad, boolean isChargable) {		
 	}
	
	private void showBanner(){
		LinearLayout layout=findViewById(R.id.mainLayout);
		CaulyAdInfo adInfo= new CaulyAdInfoBuilder("TOeplGZT").effect("Circle").reloadInterval(1).enableDefaultBannerAd(true).build();
		adView=new CaulyAdView(this);
		adView.setAdInfo(adInfo);
		layout.addView(adView,0);
	}
	
	public void info(View v){
		startActivity(new Intent(this, InfoActivity.class));
	}
	
	public void setting(View v){
		startActivity(new Intent(this, SettingsActivity.class));
	}
	
	public void buyActivity(View v){
		Intent intent = new Intent(this, BillingActivity.class);
		startActivity(intent);
	}
	
	public void errorWithUpdate(View v){
		new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.main_dialog_errorupdate))
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
	
	public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.yong.aod.MainService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
	}
}
