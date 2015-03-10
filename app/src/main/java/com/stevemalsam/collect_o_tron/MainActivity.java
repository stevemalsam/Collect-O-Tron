package com.stevemalsam.collect_o_tron;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.listButton)
    public void toList(Button button) {
        Intent intent = new Intent(this, ListGamesActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rateButton)
    public void toRate(Button button) {
        Intent intent = new Intent(this, RateGamesActivity.class);
        startActivity(intent);
    }
}
