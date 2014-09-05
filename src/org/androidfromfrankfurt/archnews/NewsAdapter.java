package org.androidfromfrankfurt.archnews;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.TextSize;
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
        	TextView tvDescription = ((TextView)v.findViewById(R.id.tv_description));
        	TextView tvLink = ((TextView)v.findViewById(R.id.tv_link));
        	
        	if (tvTitle != null) {
            	tvTitle.setText(o.getTitle());
            }
        	
        	if (tvPubDate != null) {
        		tvPubDate.setText(o.getDate().substring(0, o.getDate().length()-6));
            }
        	
        	if (tvDescription != null) {
        		// Pretty nice, Html.fromHtml parses the HTML/XML included in the feed automatically
        		Spanned description = Html.fromHtml(o.getDescription(), null, new HtmlTagHandler());
            	tvDescription.setText(description, TextView.BufferType.SPANNABLE);
            }
        	
        	if (tvLink != null) {
            	tvLink.setText(o.getLink().toExternalForm());
            	Linkify.addLinks(tvLink, Linkify.ALL);
            }
        }
        
        return v;
	}
}