package org.androidfromfrankfurt.archnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.crazyhitty.chdev.ks.rssmanager.OnRssLoadListener;
import com.crazyhitty.chdev.ks.rssmanager.RssItem;
import com.crazyhitty.chdev.ks.rssmanager.RssReader;

import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements OnRssLoadListener, AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private NewsAdapter listAdapter;

    private Snackbar snackbar;

    private int selected_distro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.dist_names));
        Spinner spinner = new Spinner(this);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        toolbar.addView(spinner, 0);

        initResources();
        initRecyclerView();
        initSwipeContainer();
    }



    private void initResources() {
        recyclerView = (RecyclerView) findViewById(R.id.news_list);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
    }

    private void initRecyclerView() {
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            listAdapter = new NewsAdapter(HomeActivity.this, null);
            recyclerView.setAdapter(listAdapter);
        }
    }

    private void initSwipeContainer() {
        if (swipeContainer != null) {
            swipeContainer.setOnRefreshListener(this);
        }
    }

    private String getLastDistro() {
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //return prefs.getString(getString(R.string.pref_distro), getString(R.string.dist_archlinux));
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_refresh) {
            onRefresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback that is run when downloading RSS completed successfully
     * @param rssItems      downloaded RSS items
     */
    @Override
    public void onSuccess(List<RssItem> rssItems) {
        swipeContainer.setRefreshing(false);

        updateList(rssItems);
    }

    /**
     * Callback that is run when downloading RSS failed
     * @param message
     */
    @Override
    public void onFailure(String message) {
        swipeContainer.setRefreshing(false);
        // debating whether or not to hide previous content on failure
        // on one hand it's cool to see the cached posts even when connection is lost
        // on the other hand when selecting a different distro, old posts are still showing
        //updateList(null);

        snackbar = Snackbar.make(findViewById(android.R.id.content).getRootView(), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh();
            }
        });
        snackbar.show();
    }

    private void updateList(@Nullable List<RssItem> items) {
        if (recyclerView != null && listAdapter != null) {
            listAdapter.updateItems(items);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_distro = position;

        onRefresh();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // nothing to do
    }

    @Override
    public void onRefresh() {
        if (snackbar != null)
            snackbar.dismiss();
        swipeContainer.setRefreshing(true);

        String feed = getResources().getStringArray(R.array.dist_urls)[selected_distro];
        // anybody knows how to skip this step? super annoying
        String[] feeds = {feed};
        new RssReader(this).showDialog(false).urls(feeds).parse(this);
    }
}