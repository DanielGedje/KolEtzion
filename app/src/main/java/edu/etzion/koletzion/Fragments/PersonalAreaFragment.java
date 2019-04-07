package edu.etzion.koletzion.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import edu.etzion.koletzion.MainActivity;
import edu.etzion.koletzion.R;
import edu.etzion.koletzion.authentication.AuthenticationActivity;
import edu.etzion.koletzion.database.BitmapSerializer;
import edu.etzion.koletzion.database.GetProfileByUserNameTask;
import edu.etzion.koletzion.database.UpdateProfileTask;
import edu.etzion.koletzion.models.Profile;
import edu.etzion.koletzion.on_back_pressed_listener.BaseBackPressedListener;
import edu.etzion.koletzion.player.VodDataSource;

import static android.app.Activity.RESULT_OK;


public class PersonalAreaFragment extends Fragment {
	private final String PROFILES_API_KEY = "ctleyeaciedgedgessithurd";
	private final String PROFILES_API_SECRET = "a31366679368d7d26408f78ab1402a23485e061e";
	private final String PROFILES_DB = "profiles";
	private final String DB_USER_NAME = "41c99d88-3264-4be5-b546-ff5a5be07dfb-bluemix";
	
	private RecyclerView rv;
	//if no profile, set user profile.
	private Profile profile;
	private ImageView imagePersonalArea;
	private TextView tvPersonalName;
	private TextView tvSignOut;
	private TextView tvAbout;
	private TextView tvSuggestContent;
	
	public static PersonalAreaFragment newInstance(Profile p) {
		
		Bundle args = new Bundle();
		args.putParcelable("profile", p);
		args.putString("flag", "f");
		PersonalAreaFragment fragment = new PersonalAreaFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findViews(view);
		tvPersonalName.setText(String.format("%s %s", profile.getFirstName(),
				profile.getLastName()));
		
		if (!profile.getUsername().equals(
				FirebaseAuth.getInstance().getCurrentUser().getEmail()
		)) {
			view.findViewById(R.id.tvSignOut).setVisibility(View.GONE);
			view.findViewById(R.id.tvSuggestContent).setVisibility(View.GONE);
			view.findViewById(R.id.tvAbout).setVisibility(View.GONE);
			view.findViewById(R.id.imagePersonalArea).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.tvSignOut).setVisibility(View.VISIBLE);
			view.findViewById(R.id.tvSuggestContent).setVisibility(View.VISIBLE);
			view.findViewById(R.id.tvAbout).setVisibility(View.VISIBLE);
			view.findViewById(R.id.imagePersonalArea).setVisibility(View.VISIBLE);
			((MainActivity) getActivity()).setOnBackPressedListener(new BaseBackPressedListener(getActivity()));
		}
		
		
		displayMyFeed();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		displayMyFeed();
		
	}
	
	private void displayMyFeed() {
		new GetProfileByUserNameTask(FirebaseAuth.getInstance().getCurrentUser().getEmail(), () -> {
			new VodDataSource(new WeakReference<>(rv), profile, false).execute();
		}).execute();
	}
	
	private void findViews(@NonNull View view) {
		rv = view.findViewById(R.id.rvProfileType);
		imagePersonalArea = view.findViewById(R.id.imagePersonalArea);
		tvPersonalName = view.findViewById(R.id.tvPersonalName);
		tvAbout = view.findViewById(R.id.tvAbout);
		tvSuggestContent = view.findViewById(R.id.tvSuggestContent);
		tvSignOut = view.findViewById(R.id.tvSignOut);
		setClickListeners();
		if (getArguments() == null) return;
		profile = getArguments().getParcelable("profile");
		imagePersonalArea.
				setImageBitmap(BitmapSerializer.
						decodeStringToBitmap(profile.getEncodedBitMapImage()));
		view.findViewById(R.id.tvChangeImage).setOnClickListener((v -> {
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, 10);
		}));
	}
	
	private void setClickListeners() {
		tvAbout.setOnClickListener((v -> {
			getActivity().getSupportFragmentManager().
					beginTransaction().replace(R.id.contentMain, new AboutFragment()).
					addToBackStack(null).commit();
		}));
		tvSignOut.setOnClickListener((v -> {
			FirebaseAuth.getInstance().signOut();
			((MainActivity) Objects.requireNonNull(getActivity())).playerFragment.stopPlayer();
			startActivity(new Intent(getContext(), AuthenticationActivity.class));
		}));
		tvSuggestContent.setOnClickListener((v -> {
			getActivity().getSupportFragmentManager().
					beginTransaction().replace(R.id.contentMain, new SuggestContentFragment()).
					addToBackStack(null).commit();
		}));
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_personal_area, container, false);
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (resultCode == RESULT_OK) {
			try {
				final Uri imageUri = data.getData();
				final InputStream imageStream = activity.getContentResolver().openInputStream(imageUri);
				final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
				imagePersonalArea.setImageBitmap(selectedImage);
				profile.setEncodedBitMapImage(BitmapSerializer.
						encodeBitmapToString(BitmapSerializer.
								getBitmapFromImageView(imagePersonalArea)));
				new GetProfileByUserNameTask(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
						p -> {
							profile.setEncodedBitMapImage(BitmapSerializer.encodeBitmapToString(
									BitmapSerializer.
											getBitmapFromImageView(imagePersonalArea)));
							new UpdateProfileTask(p).execute();
						});
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		} else {
			startActivity(new Intent(getContext(), MainActivity.class));
			Toast.makeText(getContext(), "לא נמצאה תמונה", Toast.LENGTH_LONG).show();
		}
	}
	
	
}
