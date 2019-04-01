package edu.etzion.koletzion.authentication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import edu.etzion.koletzion.MainActivity;
import edu.etzion.koletzion.R;

public class AuthenticationActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if(FirebaseAuth.getInstance().getCurrentUser() != null) startActivity(
				new Intent(this, MainActivity.class));
		getSupportFragmentManager().beginTransaction().
				replace(R.id.authenticationFrame, new LoginFragment()).commit();
	}

}
