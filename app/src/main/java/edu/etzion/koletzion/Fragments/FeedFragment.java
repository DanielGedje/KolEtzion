package edu.etzion.koletzion.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.etzion.koletzion.Adapters.rvBroadcastersAdapter;
import edu.etzion.koletzion.R;
import edu.etzion.koletzion.authentication.BroadcasterUser;
import edu.etzion.koletzion.models.Profile;
import edu.etzion.koletzion.player.VodDataSource;

public class FeedFragment extends Fragment {
	
	RecyclerView rvFeed;
	RecyclerView rvBroadcasters;
	List<Profile> broadcasters;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		broadcasters= new ArrayList<>();
		
		broadcasters.add(new Profile("yair", "frid"));
		broadcasters.add(new Profile("yossi", "appo"));
		broadcasters.add(new Profile("joe", "joy"));
		return inflater.inflate(R.layout.fragment_feed, container, false);
		
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		rvFeed = view.findViewById(R.id.rvPost);
		rvBroadcasters = view.findViewById(R.id.rvBroadcasters);
	
		rvBroadcasters.setAdapter(new rvBroadcastersAdapter(broadcasters, getContext()));
		rvBroadcasters.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		new VodDataSource(rvFeed).execute();
	}
}