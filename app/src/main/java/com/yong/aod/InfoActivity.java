package com.yong.aod;

import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }
	public void share(View v){
		Intent msg = new Intent(Intent.ACTION_SEND);
		msg.addCategory(Intent.CATEGORY_DEFAULT);
		msg.putExtra(Intent.EXTRA_SUBJECT, "Always On Display 다운로드하러가기");
		msg.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.yong.aod");
		msg.setType("text/plain");
		startActivity(Intent.createChooser(msg, "공유"));
	}
	public void blog(View v){
		Intent myIntent = new Intent
		(Intent.ACTION_VIEW, Uri.parse
		 ("http://blog.naver.com/yymin1022"));
		startActivity(myIntent);}
}
