package com.yong.aod;

import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.anjlab.android.iab.v3.*;

import androidx.appcompat.app.AppCompatActivity;

public class BillingActivity extends AppCompatActivity
{
	private static final String PRODUCT_ID1 = "donate_1000";
	private static final String PRODUCT_ID2 = "donate_5000";
	private static final String PRODUCT_ID3 = "donate_10000";
	private static final String PRODUCT_ID4 = "donate_50000";
	private static final String PRODUCT_ID5 = "ad_remove";
    private static final String LICENSE_KEY = BuildConfig.LicenseKey;
	private static final String MERCHANT_ID= BuildConfig.MerchantID;
	private static final String LOG_TAG = "yong_aod";
	private BillingProcessor bp;
	private boolean readyToPurchase = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing);
		final Button donateButton1 = findViewById(R.id.donate1);
		final Button donateButton2 = findViewById(R.id.donate2);
		final Button donateButton3 = findViewById(R.id.donate3);
		final Button donateButton4 = findViewById(R.id.donate4);
		final Button ad_removeButton = findViewById(R.id.ad_remove);
		donateButton1.setEnabled(false);
		donateButton1.setText(getString(R.string.donate_wait));
		donateButton2.setEnabled(false);
		donateButton2.setText(getString(R.string.donate_wait));
		donateButton3.setEnabled(false);
		donateButton3.setText(getString(R.string.donate_wait));
		donateButton4.setEnabled(false);
		donateButton4.setText(getString(R.string.donate_wait));
		ad_removeButton.setEnabled(false);
		ad_removeButton.setText(getString(R.string.donate_wait));
		if (!BillingProcessor.isIabServiceAvailable(this))
		{
            showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }

        bp = new BillingProcessor(this, LICENSE_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
				@Override
				public void onProductPurchased(String productId, TransactionDetails details)
				{
					switch (productId)
					{
						case "ad_remove":
							ad_removeButton.setEnabled(false);
							ad_removeButton.setText(getString(R.string.donate_already_purchased));
							SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
							SharedPreferences.Editor ed = prefs.edit();
							ed.remove("ad_removed");
							ed.putBoolean("ad_removed", true);
							ed.apply();
							break;
						case "donate_1000":
							donateButton1.setEnabled(false);
							donateButton1.setText(getString(R.string.donate_already_purchased));
							break;
						case "donate_5000":
							donateButton2.setEnabled(false);
							donateButton2.setText(getString(R.string.donate_already_purchased));
							break;
						case "donate_10000":
							donateButton3.setEnabled(false);
							donateButton3.setText(getString(R.string.donate_already_purchased));
							break;
						case "donate_50000":
							donateButton4.setEnabled(false);
							donateButton4.setText(getString(R.string.donate_already_purchased));
							break;
					}
				}
				@Override
				public void onBillingError(int errorCode, Throwable error)
				{
					showToast("Error: " + Integer.toString(errorCode));
				}
				@Override
				public void onBillingInitialized()
				{
					readyToPurchase = true;
					donateButton1.setEnabled(true);
					donateButton1.setText(getString(R.string.donate_ready));
					donateButton2.setEnabled(true);
					donateButton2.setText(getString(R.string.donate_ready));
					donateButton3.setEnabled(true);
					donateButton3.setText(getString(R.string.donate_ready));
					donateButton4.setEnabled(true);
					donateButton4.setText(getString(R.string.donate_ready));
					ad_removeButton.setEnabled(true);
					ad_removeButton.setText(getString(R.string.donate_ready));
				}
				@Override
				public void onPurchaseHistoryRestored()
				{
					for (String sku : bp.listOwnedProducts())
					{
						Log.d(LOG_TAG, "Owned Managed Product: " + sku);
						if(sku.equals("ad_remove")){
							SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
							SharedPreferences.Editor ed = prefs.edit();
							ed.remove("ad_removed");
							ed.putBoolean("ad_removed", true);
							ed.apply();
						}
					}
					for (String sku : bp.listOwnedSubscriptions())
						Log.d(LOG_TAG, "Owned Subscription: " + sku);
				}
			});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
    public void onDestroy()
	{
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String message)
	{
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

	public void donate1(View v)
	{
		if (!readyToPurchase)
		{
            showToast("Billing not initialized.");
            return;
        }
		bp.purchase(this, PRODUCT_ID1);
	}

	public void donate2(View v)
	{
		if (!readyToPurchase)
		{
            showToast("Billing not initialized.");
            return;
        }
		bp.purchase(this, PRODUCT_ID2);
	}

	public void donate3(View v)
	{
		if (!readyToPurchase)
		{
            showToast("Billing not initialized.");
            return;
        }
		bp.purchase(this, PRODUCT_ID3);
	}

	public void donate4(View v)
	{
		if (!readyToPurchase)
		{
            showToast("Billing not initialized.");
            return;
        }
		bp.purchase(this, PRODUCT_ID4);
	}

	public void ad_remove(View v)
	{
		if (!readyToPurchase)
		{
            showToast("Billing not initialized.");
            return;
        }
		bp.purchase(this, PRODUCT_ID5);
	}
}	
