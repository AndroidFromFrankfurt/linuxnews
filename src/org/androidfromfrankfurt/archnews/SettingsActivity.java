package org.androidfromfrankfurt.archnews;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		setContentView(R.layout.activity_about);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new SettingsFragment()).commit();
		}
	}

	public static class SettingsFragment extends PreferenceFragment {

		public SettingsFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.settings);
		}
	}
}
