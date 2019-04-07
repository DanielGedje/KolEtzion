package edu.etzion.koletzion.push_notification;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.backendless.push.BackendlessFCMService;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import edu.etzion.koletzion.MainActivity;

public class PushNotificationService extends BackendlessFCMService {
	@Override
	public boolean onMessage(Context appContext, Intent msgIntent) {
		Intent intent = new Intent(appContext, MainActivity.class);
		intent.putExtra("stream", true);
		if(MainActivity.receiver == null) return false;
		LocalBroadcastManager manager = LocalBroadcastManager.getInstance(appContext);
				manager.registerReceiver(MainActivity.receiver, new IntentFilter());
				manager.sendBroadcast(intent);
		return super.onMessage(appContext, msgIntent);
	}
}
