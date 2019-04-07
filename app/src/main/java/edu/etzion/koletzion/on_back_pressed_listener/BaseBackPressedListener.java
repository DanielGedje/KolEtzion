package edu.etzion.koletzion.on_back_pressed_listener;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class BaseBackPressedListener implements OnBackPressedListener {
	private final FragmentActivity activity;
	
	public BaseBackPressedListener(FragmentActivity activity) {
		this.activity = activity;
	}
	
	@Override
	public void doBack() {
		activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
}
