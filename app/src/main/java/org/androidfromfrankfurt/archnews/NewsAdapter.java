package org.androidfromfrankfurt.archnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.rssmanager.RssItem;

import java.util.ArrayList;

/**
 * Created by lesik on 6/19/16.
 */
public class NewsAdapter extends ArrayAdapter<RssItem> {

    private ArrayList<RssItem> items;
    private Context context;
    private int layout;

    public NewsAdapter(Context context, int layout, ArrayList<RssItem> items) {
        super(context, layout, items);
        this.items = items;
        this.context = context;
        this.layout = layout;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RssItem rssItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.news_item_title);
        WebView description = (WebView) convertView.findViewById(R.id.news_item_webview);

        title.setText(rssItem.getTitle());
        description.loadDataWithBaseURL(null, rssItem.getDescription(), "text/html", "UTF-8", null);
        //description.loadUrl("javascript:document.body.style.color ='white';");
        //description.setBackgroundColor(0x00000000);
        description.setLongClickable(false);

        return convertView;
    }

}
