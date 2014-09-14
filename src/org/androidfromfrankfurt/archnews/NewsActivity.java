package org.androidfromfrankfurt.archnews;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.negusoft.holoaccent.activity.AccentActivity;

public class NewsActivity extends AccentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
            		.add(R.id.container, new NewsFragment())
            		.commit();
        }
    }

    @Override
    protected void onPause() {
//    	super.onBackPressed();
    	super.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_reload) {
			NewsFragment.getInstance().startLoading();
		}
        else if (id == R.id.action_settings) {
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.feature_ideas), Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
