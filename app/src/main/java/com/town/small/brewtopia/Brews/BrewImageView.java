package com.town.small.brewtopia.Brews;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.town.small.brewtopia.AppSettings.AppSettings;
import com.town.small.brewtopia.R;

public class BrewImageView extends ActionBarActivity {

    private Toolbar toolbar;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        //toolbar
        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(BrewActivityData.getInstance().getAddEditViewBrew().getBrewName());

        imageView=(ImageView) findViewById(R.id.brewImageView);
        imageView.setImageBitmap(BrewActivityData.getInstance().getImageDisplyBitmap());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_brews, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_help:
                //showHelp();
                return true;
            case R.id.action_settings:
                showSettings();
                return true;
            default:
                onBackPressed();
                return true;
        }
    }

    private void showSettings()
    {
        //Create and intent which will open next activity AppSettings
        Intent intent = new Intent(this, AppSettings.class);
        startActivity(intent);
    }
}
