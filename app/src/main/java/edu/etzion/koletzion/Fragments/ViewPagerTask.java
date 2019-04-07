package edu.etzion.koletzion.Fragments;

import android.os.AsyncTask;
import android.util.Log;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import edu.etzion.koletzion.Adapters.ViewPagerAdapter;
import edu.etzion.koletzion.models.Profile;

public class ViewPagerTask extends AsyncTask<Void, Void, Profile> {
	private final String PROFILES_API_KEY = "ctleyeaciedgedgessithurd";
	private final String PROFILES_API_SECRET = "a31366679368d7d26408f78ab1402a23485e061e";
	private final String PROFILES_DB = "profiles";
	private final String DB_USER_NAME = "41c99d88-3264-4be5-b546-ff5a5be07dfb-bluemix";
	private WeakReference<ViewPager> vpMain;
	private ViewPagerAdapter vpMainAdapter;
	private WeakReference<TabLayout> tabLayout;
	FragmentManager fm;
	
	public ViewPagerTask(ViewPager vpMain, FragmentManager fm,TabLayout tabLayout) {
		this.vpMain = new WeakReference<>(vpMain);
		this.vpMainAdapter = new ViewPagerAdapter(fm);
		this.fm = fm;
		this.tabLayout = new WeakReference<>(tabLayout);
	}
	
	@Override
	protected Profile doInBackground(Void... voids) {
		Profile profile = null;
		CloudantClient client = ClientBuilder.account(DB_USER_NAME)
				.username(PROFILES_API_KEY)
				.password(PROFILES_API_SECRET)
				.build();
		
		Database db = client.database(PROFILES_DB, false);
		
		List<Profile> list = db.findByIndex("{\n" +
				"   \"selector\": {\n" +
				"      \"username\": \""+ FirebaseAuth.getInstance().getCurrentUser().getEmail()+"\"\n" +
				"   }\n" +
				"}", Profile.class);
		for (Profile item : list) {
			Log.e("check", "checkResult: "+item.toString());
			profile=item;
		}
		Log.e("check", list.toString());
		return profile;
	}
	
	@Override
	protected void onPostExecute(Profile profile) {
		vpMainAdapter = new ViewPagerAdapter(fm);
		vpMainAdapter.addFragment(PersonalAreaFragment.newInstance(profile),"PersonalAreaFragment");
		vpMainAdapter.addFragment(new FeedFragment(),"FeedFragment");
		vpMain.get().setAdapter(vpMainAdapter);
		tabLayout.get().addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				vpMain.get().setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		vpMain.get().addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.get()));
	}
}

