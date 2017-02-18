package org.androidfromfrankfurt.archnews;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.rssmanager.RssItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder> {

    private List<RssItem> items;

    public NewsAdapter(@Nullable List<RssItem> items) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.items = items;
    }

    class NewsItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView title, pubDate, desc;

        public NewsItemViewHolder(View view) {
            super(view);
            title   = (TextView) view.findViewById(R.id.news_item_title);
            desc    = (TextView) view.findViewById(R.id.news_item_description);
            pubDate = (TextView) view.findViewById(R.id.news_item_publish_date);
        }
    }

    @Override
    public NewsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new ViewHolder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsItemViewHolder vh, int position) {
        RssItem rssItem = getItem(position);
        if (rssItem != null) {
            vh.title.setText(rssItem.getTitle());
            vh.desc.setText(Html.fromHtml(rssItem.getDescription()));
            vh.pubDate.setText(formatPublishDate(rssItem.getPubDate()));
        }
    }

    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }

    private @Nullable RssItem getItem(int position) {
        if (items != null && position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    public void updateItems(List<RssItem> allItems) {
        items.clear();
        if (allItems != null) {
            items.addAll(allItems);
        }
        notifyDataSetChanged();
    }

    private String formatPublishDate(String publishDate) {
        if (publishDate != null) {
            Date pubDate = new Date(publishDate);
            SimpleDateFormat df = new SimpleDateFormat("dd MMM YYYY HH:mm", Locale.getDefault());
            return df.format(pubDate);
        }
        return "-";
    }
}
