package org.androidfromfrankfurt.archnews;

import java.util.Arrays;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class NewsActivity extends FragmentActivity implements OnMenuItemClickListener {

	private static NewsActivity mThis;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThis = this;
        setContentView(R.layout.activity_news);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.ic_arch);
        ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
    }

    public static NewsActivity getThis() {
    	return mThis;
    }
    
    private class TabAdapter extends FragmentPagerAdapter {
    	private TabAdapter(FragmentManager fragmentManager) {
    		super(fragmentManager);
    	}

		@Override
		public int getCount() {
			return 1;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return getResources().getString(R.string.news);
		}

		@Override
		public Fragment getItem(int arg0) {
			return new NewsFragment();
		}
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_reload) {
			NewsFragment.getInstance().startLoading();
		}
        else if(id == R.id.action_lang) {
        	View langMenuItem = findViewById(R.id.action_lang);
        	PopupMenu langMenu = new PopupMenu(getApplicationContext(), langMenuItem);
    		for(int i=0; i < getResources().getStringArray(R.array.lang).length; i++) {
    			langMenu.getMenu().add(getResources().getStringArray(R.array.lang)[i]);
    		}
    		langMenu.setOnMenuItemClickListener(this);
    		langMenu.show();
        }
        else if(id == R.id.action_about) {
        	Intent intent = new Intent(this, AboutActivity.class);
        	startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		String[] langArray = getResources().getStringArray(R.array.lang);
		// Get position of the language in the array
		int posInArray = Arrays.asList(langArray).indexOf(item.getTitle());
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = sharedPrefs.edit();
		// Get previously selected language, 0 if nothing was selected yet
		int oldLang = sharedPrefs.getInt("lang", 0);
		System.out.println("SetPref "+posInArray);
    	editor.putInt("lang", posInArray);
		editor.commit();
		
		// If previously selected language is equal to the currently selected, do nothing. Else, reload.
//		if(item.getItemId() != oldLang) {
			NewsFragment.getInstance().startLoading();
//		}
		return false;
	}
}
