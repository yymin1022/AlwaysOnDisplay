package com.yong.aod;

import android.os.Build;
import android.service.quicksettings.*;
import android.content.*;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ToggleService extends TileService
{
	boolean isListening = false;
	
	@Override
	public void onStartListening()
	{
		super.onStartListening();
		isListening = true;
	}

	@Override
	public void onStopListening()
	{
		super.onStopListening();
		isListening = false;
	}
	
	@Override
    public void onClick() {
		Tile tile = getQsTile();
        int tileState = tile.getState();
        if (tileState != Tile.STATE_UNAVAILABLE) {
            tile.setState(tileState == Tile.STATE_ACTIVE? Tile.STATE_INACTIVE : Tile.STATE_ACTIVE);
            tile.updateTile();
			if (tileState == Tile.STATE_ACTIVE) {
                stopService(new Intent(this, MainService.class));
            }else if(tileState == Tile.STATE_INACTIVE){
				startService(new Intent(this, MainService.class));
			}
        }
    }
}
