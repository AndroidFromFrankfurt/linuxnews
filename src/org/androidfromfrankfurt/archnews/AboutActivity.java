package org.androidfromfrankfurt.archnews;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		setContentView(R.layout.activity_about);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new AboutFragment()).commit();
		}
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//			case android.R.id.home:
//				finish();
//				return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	public static class AboutFragment extends PreferenceFragment {

		public AboutFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.about);
		}
	}
}
