package org.androidfromfrankfurt.archnews;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.LangUtils;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import at.theengine.android.simple_rss2_android.RSSItem;
import at.theengine.android.simple_rss2_android.SimpleRss2Parser;
import at.theengine.android.simple_rss2_android.SimpleRss2ParserCallback;

public class NewsActivity extends ListActivity implements OnNavigationListener {

	private ActionBar actionBar;
	private MultiStateListView listView;
	private View headerView;
	private TextView tvErrorMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// ListView
		listView = new MultiStateListView.Builder(this)
			.loadingView(R.layout.loading)
			.errorView(R.layout.error)
			.build();
		listView.setId(android.R.id.list);
		listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_INSET);
		listView.setDivider(null);
		listView.setDividerHeight(10);
		setContentView(listView);
		
		LayoutParams listViewParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		listViewParams.setMargins(20, 0, 0, 0);
		getListView().setLayoutParams(listViewParams);
		
		// ListView Header
		headerView = getLayoutInflater().inflate(R.layout.header, null, false);
		getListView().addHeaderView(headerView);
		
		// ActionBar
		actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<CharSequence> distroAdapter = ArrayAdapter.createFromResource(this, R.array.distros, R.layout.nav_spinner_item);
        actionBar.setListNavigationCallbacks(distroAdapter, this);
        
        // Layout
        tvErrorMessage = (TextView)listView.getErrorView().findViewById(R.id.tv_errormessage);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	int id = item.getItemId();
        if(id == R.id.action_reload) {
        	
        }
        else if(id == R.id.action_settings) {
        	Intent intent = new Intent(this, SettingsActivity.class);
        	startActivity(intent);
            return true;        	
        }
        else if(id == R.id.action_about) {
        	Intent intent = new Intent(this, AboutActivity.class);
        	startActivity(intent);
            return true;
        }
    	return super.onOptionsItemSelected(item);
    }

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
    	Resources resources = getResources();
    	String selectedDistro = resources.getStringArray(R.array.distros)[itemPosition];
		String selectedLang = PreferenceManager.getDefaultSharedPreferences(this).getString(resources.getString(R.string.pref_key_language), "English");
    	Drawable distroIcon = null;
    	String distroFeedUrl;
    	int distroColor;
    	
    	if(selectedDistro.equals("Debian")) {
    		distroIcon = resources.getDrawable(R.drawable.ic_distro_debian);
    		distroColor = resources.getColor(R.color.distro_color_debian);
    		distroFeedUrl = resources.getString(R.string.distro_url_debian);
    	}
    	else if(selectedDistro.equals("Gentoo")) {
    		distroIcon = resources.getDrawable(R.drawable.ic_distro_gentoo);
    		distroColor = resources.getColor(R.color.distro_color_gentoo);
    		distroFeedUrl = resources.getString(R.string.distro_url_gentoo);
    	}
    	else if(selectedDistro.equals("Parabola")) {
    		distroIcon = resources.getDrawable(R.drawable.ic_distro_linux);
    		distroColor = resources.getColor(R.color.distro_color_parabola);
    		distroFeedUrl = resources.getString(R.string.distro_url_parabola);
    	}
    	else {
    		// default (else) is Arch Linux
    		distroIcon = resources.getDrawable(R.drawable.ic_distro_arch);
    		distroColor = resources.getColor(R.color.distro_color_arch);
    		System.out.println(selectedLang);
    		if(selectedLang.equals(resources.getStringArray(R.array.languages)[1])) {
    			distroFeedUrl = resources.getString(R.string.distro_url_arch_de);
    			System.out.println(resources.getString(R.string.distro_url_arch_de));
    		}
    		else if(selectedLang.equals(resources.getStringArray(R.array.languages)[2])) {
    			distroFeedUrl = resources.getString(R.string.distro_url_arch_fr);
    		}
    		else if(selectedLang.equals(resources.getStringArray(R.array.languages)[3])) {
    			distroFeedUrl = resources.getString(R.string.distro_url_arch_es);
    		}
    		else if(selectedLang.equals(resources.getStringArray(R.array.languages)[4])) {
    			distroFeedUrl = resources.getString(R.string.distro_url_arch_ru);
    		}
    		else if(selectedLang.equals(resources.getStringArray(R.array.languages)[5])) {
    			distroFeedUrl = resources.getString(R.string.distro_url_arch_br);
    		}
    		else if(selectedLang.equals(resources.getStringArray(R.array.languages)[6])) {
    			distroFeedUrl = resources.getString(R.string.distro_url_arch_cn);
    		}
    		else if(selectedLang.equals(resources.getStringArray(R.array.languages)[7])) {
    			distroFeedUrl = resources.getString(R.string.distro_url_arch_ro);
    		}
    		else {
    			distroFeedUrl = resources.getString(R.string.distro_url_arch);
    		}
    	}
		getActionBar().setIcon(distroIcon);
		// Doesn't look good, disable for now
//    	getActionBar().setBackgroundDrawable(new ColorDrawable(distroColor));
		parseRss(distroFeedUrl);
		final Uri distroFeedUri = Uri.parse(distroFeedUrl);
		headerView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_VIEW, distroFeedUri);
				startActivity(intent);
			}
		});
    	return true;
	}
	
    private void parseRss(String urlToParse) {
    	// show loading dialog
    	listView.setAdapter(null);
		listView.showLoadingView();
				
		SimpleRss2Parser newsParser = new SimpleRss2Parser(urlToParse,
    			new SimpleRss2ParserCallback() {
			
			@Override
			public void onFeedParsed(List<RSSItem> arg0) {
				setListAdapter(new NewsAdapter(getApplicationContext(), R.layout.news_item, (ArrayList<RSSItem>) arg0));
				loadingFinished(true, null);
			}
			
			@Override
			public void onError(Exception arg0) {
				listView.showErrorView();
	    		tvErrorMessage.setText(arg0.getMessage());
			}
		});
    	newsParser.parseAsync();
    }
    
    private void loadingFinished(boolean successful, String errorMessage) {
    	if (!successful && errorMessage != null) {
    		
    	}
    }
}
