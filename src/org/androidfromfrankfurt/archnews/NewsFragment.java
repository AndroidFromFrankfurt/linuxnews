package org.androidfromfrankfurt.archnews;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import at.theengine.android.simple_rss2_android.RSSItem;
import at.theengine.android.simple_rss2_android.SimpleRss2Parser;
import at.theengine.android.simple_rss2_android.SimpleRss2ParserCallback;


public class NewsFragment extends Fragment {

    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        parseRss();
        return rootView;
    }
    
    private void parseRss() {
    	SimpleRss2Parser pars = new SimpleRss2Parser("https://www.archlinux.org/feeds/news/",
    			new SimpleRss2ParserCallback() {
			
			@Override
			public void onFeedParsed(List<RSSItem> arg0) {
				// TODO Auto-generated method stub
				for(int i = 0; i < arg0.size(); i++){
	                Log.d("SimpleRss2ParserDemo",arg0.get(i).getTitle());
	            }				
			}
			
			@Override
			public void onError(Exception arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity().getApplicationContext(),
						arg0.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		});
    }
}