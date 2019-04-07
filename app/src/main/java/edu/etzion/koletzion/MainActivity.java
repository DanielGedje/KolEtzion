package edu.etzion.koletzion;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.push.DeviceRegistrationResult;
import com.google.firebase.auth.FirebaseAuth;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import edu.etzion.koletzion.Fragments.MainViewPagerFragment;
import edu.etzion.koletzion.Fragments.MoodFragment;
import edu.etzion.koletzion.authentication.AuthenticationActivity;
import edu.etzion.koletzion.on_back_pressed_listener.OnBackPressedListener;
import edu.etzion.koletzion.player.ExoPlayerFragment;
import edu.etzion.koletzion.player.StartLiveStreamTask;
import edu.etzion.koletzion.push_notification.PushNotificationReceiver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	
	private ArrayList<String> permissions = new ArrayList<>(
			Arrays.asList(Manifest.permission.GET_ACCOUNTS,
					Manifest.permission.READ_CONTACTS,
					Manifest.permission.WAKE_LOCK,
					Manifest.permission.READ_EXTERNAL_STORAGE));
	private FirebaseAuth auth;
	public ExoPlayerFragment playerFragment;
	public FrameLayout frame;
	private Toolbar toolbar;
	private DrawerLayout drawer;
	protected OnBackPressedListener onBackPressedListener;
	
	private SharedPreferences sp;
	private SharedPreferences.Editor spEditor;
	
	
	public static PushNotificationReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSupportActionBar(toolbar);


//		moodPopUp();

		main();
		// Enable Notification Channel for Android OREO
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//			startForegroundService(new Intent(this, ForegroundService.class));
//		}else{
//			startService(new Intent(this, ForegroundService.class));
//		}
		//init backendless
		initBackendless();
		
		
	}
	
	private void askForPermissionsIfNeeded() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
				permissions.add(Manifest.permission.FOREGROUND_SERVICE);
			}
			for (String permission : permissions) {
				if (ContextCompat.checkSelfPermission(this, permission) !=
						PackageManager.PERMISSION_GRANTED) {
					ArrayList<String> strings = new ArrayList<>();
					strings.add(permission);
					requestPermissions(strings.
							toArray(new String[]{}), 0);
				}
			}
		}
	}
	
	private void moodPopUp() {
		sp = getSharedPreferences("LocalData", MODE_PRIVATE);
		spEditor = sp.edit();
		if (!sp.contains("userName")) {
			createUserOnSP();
			new MoodFragment().show(getSupportFragmentManager(), "tag");
		} else {
			if (!sp.getString("dateStamp", "").equals(LocalDate.now().toString())) {
				new MoodFragment().show(getSupportFragmentManager(), "tag");
			}
		}
	}
	
	private void createUserOnSP() {
		spEditor.putString("userName", FirebaseAuth.getInstance().getCurrentUser().getEmail());
		spEditor.putString("dateStamp", LocalDate.now().toString());
	}
	
	private void initBackendless() {
		Backendless.initApp(this, "B004AE57-963C-4667-FFAF-B3A5C251F100",
				"C6A4B36A-A709-7AB2-FF1B-B5CDC4CAD200");
		
		List<String> channels = new ArrayList<>();
		channels.add("default");
		Backendless.Messaging.registerDevice(channels, new AsyncCallback<DeviceRegistrationResult>() {
			@Override
			public void handleResponse(DeviceRegistrationResult response) {
			
			}
			
			@Override
			public void handleFault(BackendlessFault fault) {
			
			}
		});
	}
	
	
	private void main() {
		askForPermissionsIfNeeded();
		findViews();
		startAuthenticationActivityIfNeeded();
		initFragments();
	}
	
	private void initFragments() {
		getSupportFragmentManager().beginTransaction().replace(frame.getId(),
				playerFragment).commit();
		frame.setVisibility(View.GONE);
		getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new MainViewPagerFragment()).commit();
	}
	
	
	private void startAuthenticationActivityIfNeeded() {
		if (auth.getCurrentUser() == null) {
			startActivity(new Intent(this, AuthenticationActivity.class));
			finish();
		}
	}
	
	
	private void findViews() {
		auth = FirebaseAuth.getInstance();
		frame = findViewById(R.id.frame);
		toolbar = findViewById(R.id.toolbar);
		playerFragment = new ExoPlayerFragment();
		findViewById(R.id.btnLiveStream).setOnClickListener(this);
		receiver = new PushNotificationReceiver(findViewById(R.id.btnLiveStream));
	}
	
	
	public void initPlayer(String filePath) {
		playerFragment.initPlayer(filePath);
	}
	
	@Override
	public void onBackPressed() {
		if (onBackPressedListener != null)
			onBackPressedListener.doBack();
		else
			super.onBackPressed();
	}
	
	public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
		this.onBackPressedListener = onBackPressedListener;
	}
	
	@Override
	public void onClick(View v) {
		new StartLiveStreamTask(playerFragment).execute();
//		v.setOnClickListener(null);
//		final Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				v.setOnClickListener(activity);
//			}
//		}, 3500);
	}
}
