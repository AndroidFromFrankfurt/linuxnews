package org.androidfromfrankfurt.archnews;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.rssmanager.RssItem;

import org.androidfromfrankfurt.archnews.utils.FormatUtil;
import org.androidfromfrankfurt.archnews.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder> {

    private List<RssItem> items;
    private Context context;

    public NewsAdapter(@NonNull Context context, @Nullable List<RssItem> items) {
        this.context = context;

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
            vh.pubDate.setText(FormatUtil.formatPublishDate(rssItem.getPubDate()));
            vh.itemView.setTag(rssItem);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openInBrowser((RssItem) v.getTag());
                }
            });
        }
    }

    private void openInBrowser(RssItem rssItem) {
        Intent intent = IntentUtil.getWebIntent(rssItem.getLink());
        if (intent != null) {
            context.startActivity(intent);
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
}
