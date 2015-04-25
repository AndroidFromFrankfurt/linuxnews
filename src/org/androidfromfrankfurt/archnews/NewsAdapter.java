package org.androidfromfrankfurt.archnews;

import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import at.theengine.android.simple_rss2_android.RSSItem;

class NewsAdapter extends ArrayAdapter<RSSItem> {

    private ArrayList<RSSItem> items;
    private Context ctx;
    private int layout;

    public NewsAdapter(Context context, int layout, ArrayList<RSSItem> items) {
        super(context, layout, items);
        this.items = items;
        this.ctx = context;
        this.layout = layout;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layout, null);
        }
        
        RSSItem o = items.get(position);
        if (o != null) {
        	TextView tvTitle = ((TextView)v.findViewById(R.id.tv_title));
        	TextView tvPubDate = ((TextView)v.findViewById(R.id.tv_pubdate));
        	WebView tvDescription = ((WebView)v.findViewById(R.id.wv_description));
        	
        	if (tvTitle != null) {
            	tvTitle.setText(o.getTitle());
            	final URL url = o.getLink();
        		tvTitle.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ctx.startActivity(new Intent(Intent.ACTION_VIEW).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setData(Uri.parse(url.toString())));
					}
				});
            }
        	
        	if (tvPubDate != null) {
        		try {
        			// too stupid to turn the RSS date into a local date - just delete some chars and it'll be fine ;)
            		tvPubDate.setText(o.getDate().substring(5, o.getDate().length()-15));
        		}
        		catch(Exception e) {
        			tvPubDate.setText("");
        		}
            }
        	
        	if (tvDescription != null) {
            	tvDescription.loadDataWithBaseURL(null, o.getDescription(), "text/html", "UTF-8", null);
            	tvDescription.setBackgroundColor(0x00000000);
            }
        }
        
        return v;
	}
}