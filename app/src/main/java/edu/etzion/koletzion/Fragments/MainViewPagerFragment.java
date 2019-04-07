package edu.etzion.koletzion.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import edu.etzion.koletzion.Adapters.ViewPagerAdapter;
import edu.etzion.koletzion.R;

public class MainViewPagerFragment extends Fragment {
	
	public ViewPager vpMain;
	private TabLayout tabLayout;
	public MainViewPagerFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		
		return inflater.inflate(R.layout.fragment_main_view_pager, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		vpMain = view.findViewById(R.id.vpMain);
		tabLayout = view.findViewById(R.id.tabLayout);
		vpMain.setCurrentItem(0);

	}
	
	@Override
	public void onResume() {
		super.onResume();
		ViewPagerAdapterMainActivity();
	}
	
	private void ViewPagerAdapterMainActivity() {
		//this method includes the viewpager adapter that includes all the mainactivity fragments.
		// getting the current latest update profile object from the server
		new ViewPagerTask(vpMain, getChildFragmentManager(),tabLayout).execute();
		
	}
}
