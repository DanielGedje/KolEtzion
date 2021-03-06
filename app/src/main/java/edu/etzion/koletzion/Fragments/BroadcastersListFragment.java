package edu.etzion.koletzion.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import edu.etzion.koletzion.R;
import edu.etzion.koletzion.database.BroadcastersDataSource;
import edu.etzion.koletzion.models.Profile;

/**
 * A simple {@link Fragment} subclass.
 */
public class BroadcastersListFragment extends Fragment {
	
	private RecyclerView rvBroadcastersList;
	private List<Profile> broadcasters;
	
	public BroadcastersListFragment() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		
		return inflater.inflate(R.layout.fragment_broadcasters_list, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		rvBroadcastersList = view.findViewById(R.id.rvBroadcastersList);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		new BroadcastersDataSource(new WeakReference<>(rvBroadcastersList), true, this).execute();
	}
}
