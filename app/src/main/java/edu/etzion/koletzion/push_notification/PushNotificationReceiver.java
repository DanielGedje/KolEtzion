package edu.etzion.koletzion.push_notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class PushNotificationReceiver extends BroadcastReceiver {
	WeakReference<ImageView> btn;
	
	public PushNotificationReceiver(ImageView btn) {
		this.btn = new WeakReference<>(btn);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		btn.get().setBackgroundColor(0x9a0007);
	}
}
