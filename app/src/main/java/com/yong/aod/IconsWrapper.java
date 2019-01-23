package com.yong.aod;
import android.content.*;
import android.widget.*;
import android.util.*;
import java.util.*;
import android.graphics.drawable.*;
import android.graphics.*;
import android.view.*;
import android.app.*;

public class IconsWrapper extends LinearLayout {
    private Context context;

    public IconsWrapper(Context context) {
        super(context);
        this.context = context;
    }

    public IconsWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public IconsWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void update(int textColor, final Runnable action) {
        removeAllViews();
        for (final Map.Entry<String, NotificationListener.NotificationHolder> entry : Globals.notifications.entrySet()) {
            Drawable drawable = entry.getValue().getIcon(context);
            if (drawable != null) {
                drawable.setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
                final ImageView icon = new ImageView(getContext());
                icon.setImageDrawable(drawable);
                icon.setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
                final LayoutParams iconLayoutParams = new LayoutParams(96, 96, Gravity.CENTER);
                icon.setPadding(12, 0, 12, 0);
                icon.setLayoutParams(iconLayoutParams);
                //icon.setOnClickListener(view -> messageBox.showNotification(entry.getValue()));
                addView(icon);
            }
        }
    }

    /*public void setMessageBox(MessageBox messageBox) {
        this.messageBox = messageBox;
    }*/
}
