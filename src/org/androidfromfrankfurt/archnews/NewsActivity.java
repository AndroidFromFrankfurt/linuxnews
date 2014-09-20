package org.androidfromfrankfurt.archnews;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

public class NewsActivity extends FragmentActivity implements OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.ic_arch);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
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
        	View langMenuView = findViewById(R.id.action_lang);
        	PopupMenu langMenu = new PopupMenu(getApplicationContext(), langMenuView);
    		langMenu.inflate(R.menu.lang);
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
		Toast.makeText(getApplicationContext(), "workz", Toast.LENGTH_SHORT).show();
		return false;
	}
}
