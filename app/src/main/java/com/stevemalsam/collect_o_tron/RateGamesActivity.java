package com.stevemalsam.collect_o_tron;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class RateGamesActivity extends ActionBarActivity implements ListGamesFragment.Callbacks{

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_games);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(findViewById(R.id.game_detail_container) != null) {
            mTwoPane = true;

            ((ListGamesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.game_list))
                .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id) {
    }

    @Override
    public boolean shouldDisplayRating() {
        return true;
    }
}
