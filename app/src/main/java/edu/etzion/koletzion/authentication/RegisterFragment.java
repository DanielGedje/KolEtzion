package edu.etzion.koletzion.authentication;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.etzion.koletzion.MainActivity;
import edu.etzion.koletzion.R;
import edu.etzion.koletzion.database.DataDAO;
import edu.etzion.koletzion.models.BroadcastPost;
import edu.etzion.koletzion.models.Profile;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class RegisterFragment extends Fragment implements Button.OnClickListener {
	private EditText etRegisterName;
	private EditText etRegisterLastName;
	private EditText etRegisterEmail;
	private EditText etRegisterPassword;
	private EditText etRegisterConfirmPassword;
	private Button btnRegisterSubmit;
	private boolean match;
	private FirebaseAuth auth;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_register, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		FirebaseApp.initializeApp(getContext());
		findViews(view);
		btnRegisterSubmit.setOnClickListener(this);
		etRegisterConfirmPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			
			}
			
			
			@Override
			public void afterTextChanged(Editable s) {
				String password = etRegisterPassword.getText().toString().trim();
				String confirmPassword = etRegisterConfirmPassword.getText().toString().trim();
				if (password.matches(s.toString())) {
					match = true;
					etRegisterPassword.setError(null);
					
				} else {
					match = false;
					etRegisterPassword.setError("סיסמאות לא תואמות, נסה שנית");
					etRegisterConfirmPassword.requestFocus();
				}
			}
		});
		super.onViewCreated(view, savedInstanceState);
		
	}
	
	private void findViews(@NonNull View view) {
		auth = FirebaseAuth.getInstance();
		match = false;
		etRegisterName = view.findViewById(R.id.etRegisterName);
		etRegisterLastName = view.findViewById(R.id.etRegisterLastName);
		etRegisterEmail = view.findViewById(R.id.etRegisterEmail);
		etRegisterPassword = view.findViewById(R.id.etRegisterPassword);
		etRegisterConfirmPassword = view.findViewById(R.id.etRegisterConfirmPassword);
		btnRegisterSubmit = view.findViewById(R.id.btnRegisterSubmit);
	}
	
	//button onclick
	@Override
	public void onClick(View v) {
		String name = etRegisterName.getText().toString().trim();
		String lastName = etRegisterLastName.getText().toString().trim();
		String email = etRegisterEmail.getText().toString().trim();
		String password = etRegisterPassword.getText().toString().trim();
		String confirmPassword = etRegisterConfirmPassword.getText().toString().trim();
		if (name.length() <= 1) {
			etRegisterName.setError("שם קצר מדי");
			etRegisterName.requestFocus();
		} else if (lastName.length() <= 1) {
			etRegisterLastName.setError("שם משפחה קצר מדי");
			etRegisterLastName.requestFocus();
		} else if (!isEmailValid(email)) {
			etRegisterEmail.setError("מייל לא תקין");
			etRegisterEmail.requestFocus();
		} else if (password.length() < 6) {
			etRegisterPassword.setError("על הסיסמא להיות לפחות 6 אותיות או ספרות");
			etRegisterPassword.requestFocus();
		} else if (!password.equals(confirmPassword)) {
			match = false;
		} else {
			match = true;
			createUser(email, name, lastName, password);
		}
	}
	
	public void createUser(String email, String firstName, String lastName, String password) {
		auth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
					FirebaseUser currentUser = auth.getCurrentUser();
					if (task.isSuccessful()) {
						//creating a new profile and saving to the server.
						List<BroadcastPost> relatedBroadcasts = new ArrayList<>();
						Profile profile = new Profile(email, firstName, lastName, false, relatedBroadcasts, false, Profile.MOOD_NONE);
						DataDAO.getInstance().writeMyProfile(profile);
						// Sign in success, update UI with the signed-in user's information
						startActivity(new Intent(getContext(), MainActivity.class));
						getActivity().finish();
						Log.d(TAG, "createUserWithEmail:success");
					} else {
						if (task.getException() instanceof FirebaseAuthUserCollisionException) {
							Toast.makeText(getContext(), "מייל כבר נמצא במערכת", Toast.LENGTH_SHORT).show();
							return;
						}
						// If sign in fails, display a message to the user.
						Log.w(TAG, "createUserWithEmail:failure", task.getException());
						toast("הרשמה נכשלה");
					}
				});
		
	}
	
	private void toast(String text) {
		//todo only do this if keyboard is open, else do normal toast
		Toast t = Toast.makeText(getContext(), text,
				Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		t.show();
	}
	
	boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
}
