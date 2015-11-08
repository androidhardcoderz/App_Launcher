package com.applauncher.application;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.appbrain.AppBrain;

public class MainActivity extends AppCompatActivity {

    View coordinatorLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppBrain.init(this);
        setContentView(R.layout.activity_main);

        //assign UI elements
        coordinatorLayoutView = (CoordinatorLayout) findViewById(R.id.cordView);

        //build toolbar in place of action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE); //set dynamic color
        setSupportActionBar(toolbar); //attach to actionbar in place

        //check if first run
        if(UserPreferences.isFirstRun(this)) {
            showSnackBar(); //show message
            UserPreferences.setFirstRun(this,false); //set flag for first run
        }

    }

    private void showSnackBar(){
        Snackbar
                .make(coordinatorLayoutView, "Select An Application To Launch It From The List", Snackbar.LENGTH_LONG)
                .show();
    }

}
