package org.androidfromfrankfurt.archnews;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import at.theengine.android.simple_rss2_android.RSSItem;
import at.theengine.android.simple_rss2_android.SimpleRss2Parser;
import at.theengine.android.simple_rss2_android.SimpleRss2ParserCallback;


public class NewsFragment extends ListFragment implements OnScrollListener {

	private static NewsFragment instance;
	private ListView listView;
	private View headerView;
	private View errorView;
	private TextView tvError;
	private Button btnReload;
	private ProgressDialog loadingDialog;
	private boolean listVisible;
	private boolean newActivity = true;
	private int mLastFirstVisibleItem;
	
    public NewsFragment() {
    	instance = this;
    }

    public static NewsFragment getInstance() {
    	return instance;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.fragment_news, container, false);
    	listView = (ListView)rootView.findViewById(android.R.id.list);
    	headerView = inflater.inflate(R.layout.header, null);
//    	Using "null" as parameter here is not good coding behaviour, but when I use the following
//    	headerView = inflater.inflate(R.layout.header, listView, false);
//    	then some WebViews in the list items weirdly change their size after resuming the activity.
//    	Thus I will just use null to not have this glitch until someone comes up with a better solution.
    	listView.addHeaderView(headerView);
    	listView.setOnScrollListener(this);
    	tvError = (TextView)rootView.findViewById(R.id.tv_errormessage);
    	btnReload = (Button)rootView.findViewById(R.id.btn_reload);
    	return rootView;
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	initialize();
    	if(newActivity) {
    		startLoading();
    	}
    }
    
    private void initialize() {
    	errorView = getListView().getEmptyView();
    	errorView.setVisibility(View.GONE);;
    	btnReload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startLoading();
			}
		});
    	loadingDialog = new ProgressDialog(getActivity());
    	loadingDialog.setTitle(getResources().getText(R.string.dlg_loading_title));
    	loadingDialog.setMessage(getResources().getText(R.string.dlg_loading));
    	loadingDialog.setIndeterminate(true);
    	loadingDialog.setCancelable(false);
    	loadingDialog.setCanceledOnTouchOutside(false);
    }
    
    private void parseRss() {
    	// ***Do not call this method directly! Use startLoading()!***
    	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    	String feedUrl;
    	System.out.println("GetPref "+sharedPrefs.getInt("lang", 0));
    	int selectedLang = sharedPrefs.getInt("lang", 0);
    	if(selectedLang == 0) {
    		// English
    		feedUrl = "https://www.archlinux.org/feeds/news/";
    	}
    	else if(selectedLang == 1) {
    		// German
    		feedUrl = "https://bbs.archlinux.de/extern.php?action=feed&fid=257&type=atom&order=posted&show=15";
    	}
    	else if(selectedLang == 2) {
    		// French
    		feedUrl = "https://archlinux.fr/feed";
    	}
    	else if(selectedLang == 3) {
    		// Spanish
    		feedUrl = "http://portada.archlinux-es.org/feed";
    	}
    	else {
    		// Default (English)
    		feedUrl = "https://www.archlinux.org/feeds/news/";
    	}
    	SimpleRss2Parser newsParser = new SimpleRss2Parser(feedUrl,
    			new SimpleRss2ParserCallback() {
			
			@Override
			public void onFeedParsed(List<RSSItem> arg0) {
				setListAdapter(new NewsAdapter(getActivity(), R.layout.news_item, (ArrayList<RSSItem>) arg0));
				loadingSuccessful(true, null);
			}
			
			@Override
			public void onError(Exception arg0) {
				loadingSuccessful(false, arg0.getMessage());
			}
		});
    	newsParser.parseAsync();
    }
    
    public void startLoading() {
    	loadingDialog.show();
    	parseRss();
    }
    
    private void loadingSuccessful(boolean success, String errorMessage) {
    	newActivity = false;
    	loadingDialog.dismiss();
    	if (!success && errorMessage != null) {
    		showError();
    		showErrorMessage(errorMessage);
    	}
    	else {
    		hideError();
    	}
    }
    
    private void showErrorMessage(String errorMessage) {
    	tvError.setText(errorMessage);
    }
    
    private void showError() {
    	errorVisibile(true);
    }
    
    private void hideError() {
    	errorVisibile(false);
    }
    
    private void errorVisibile(boolean visible){
        if (listVisible == visible) {
            return;
        }
        listVisible = visible;
        if (visible) {
        	// Hide ListView, show error
        	listView.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_out));
            errorView.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_in));
            listView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.VISIBLE);
        }
        else {
        	// Hide error, show ListView
            errorView.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_out));
            listView.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_in));
            errorView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }
    
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		NewsActivity newsActivity = NewsActivity.getThis();
		ActionBar actionBar = newsActivity.getActionBar();
		if (mLastFirstVisibleItem < firstVisibleItem && actionBar.isShowing()) {
            actionBar.hide();
        }

        if (mLastFirstVisibleItem > firstVisibleItem && !actionBar.isShowing()) {
            actionBar.show();
        }
        mLastFirstVisibleItem = firstVisibleItem;
	}
}