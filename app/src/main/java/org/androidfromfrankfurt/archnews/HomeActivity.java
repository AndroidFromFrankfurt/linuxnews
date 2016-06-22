package org.androidfromfrankfurt.archnews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.crazyhitty.chdev.ks.rssmanager.OnRssLoadListener;
import com.crazyhitty.chdev.ks.rssmanager.RssItem;
import com.crazyhitty.chdev.ks.rssmanager.RssReader;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnRssLoadListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.ui_navigation_drawer_open, R.string.ui_navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // check last selected distro (defaults to Arch Linux)
        navigationView.getMenu().findItem(getIdByDistro(getLastDistro())).setChecked(true);

        listView = (ListView) findViewById(R.id.news_list);
        listView.setEmptyView(findViewById(R.id.news_list_empty));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        refresh();
    }

    private String getLastDistro() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(getString(R.string.pref_distro), getString(R.string.dist_archlinux));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String distro = getDistroById(id);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString(getString(R.string.pref_distro), distro).commit();

        refresh(distro);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getDistroById(int id) {
        // this is really retarded, if anybody knows a better way to handle this please contribute
        switch (id) {
            case R.id.dist_archlinux:
                return getString(R.string.dist_archlinux);
            case R.id.dist_centos:
                return getString(R.string.dist_centos);
            case R.id.dist_debian:
                return getString(R.string.dist_debian);
            case R.id.dist_gnewsense:
                return getString(R.string.dist_gnewsense);
            case R.id.dist_parabola:
                return getString(R.string.dist_parabola);
            case R.id.dist_scientificlinux:
                return getString(R.string.dist_scientificlinux);
            case R.id.dist_ubuntu:
                return getString(R.string.dist_ubuntu);
            default:
                return getString(R.string.dist_archlinux);
        }
    }

    private int getIdByDistro(String distro) {
        if (distro.equals(getString(R.string.dist_archlinux))) {
            return R.id.dist_archlinux;
        } else if (distro.equals(getString(R.string.dist_centos))) {
            return R.id.dist_centos;
        } else if (distro.equals(getString(R.string.dist_debian))) {
            return R.id.dist_debian;
        } else if (distro.equals(getString(R.string.dist_gnewsense))) {
            return R.id.dist_gnewsense;
        } else if (distro.equals(getString(R.string.dist_parabola))) {
            return R.id.dist_parabola;
        } else if (distro.equals(getString(R.string.dist_scientificlinux))) {
            return R.id.dist_scientificlinux;
        } else if (distro.equals(getString(R.string.dist_ubuntu))) {
            return R.id.dist_ubuntu;
        } else {
            return R.id.dist_archlinux;
        }
    }

    private String getFeedByDistro(String distro) {
        if (distro.equals(getString(R.string.dist_archlinux))) {
            return getString(R.string.feed_archlinux);
        } else if (distro.equals(getString(R.string.dist_centos))) {
            return getString(R.string.feed_centos);
        } else if (distro.equals(getString(R.string.dist_debian))) {
            return getString(R.string.feed_debian);
        } else if (distro.equals(getString(R.string.dist_gnewsense))) {
            return getString(R.string.feed_gnewsense);
        } else if (distro.equals(getString(R.string.dist_parabola))) {
            return getString(R.string.feed_parabola);
        } else if (distro.equals(getString(R.string.dist_scientificlinux))) {
            return getString(R.string.feed_scientificlinux);
        } else if (distro.equals(getString(R.string.dist_ubuntu))) {
            return getString(R.string.feed_ubuntu);
        } else {
            return getString(R.string.feed_archlinux);
        }
    }

    private void refresh() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String distro = prefs.getString(getString(R.string.pref_distro),
                getString(R.string.dist_archlinux));
        refresh(distro);
    }

    private void refresh(String distro) {
        listView.setAdapter(null);

        String feed = getFeedByDistro(distro);
        String[] feeds = {feed};
        new RssReader(this).showDialog(false).urls(feeds).parse(this);
    }

    @Override
    public void onSuccess(List<RssItem> rssItems) {
        listView.setAdapter(new NewsAdapter(getApplicationContext(), R.layout.news_item,
                (ArrayList<RssItem>) rssItems));
    }

    @Override
    public void onFailure(String message) {
        Snackbar.make(findViewById(android.R.id.content).getRootView(), "Failed to fetch news", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refresh();
                    }
                }).show();
    }
}
