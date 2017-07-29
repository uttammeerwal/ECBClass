package com.ecbclass.user_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ecbclass.database.Branch;
import com.ecbclass.database.MainActivity;


import com.google.firebase.quickstart.database.R;

import static com.google.firebase.quickstart.database.R.layout.activity_home;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static Context mAppContext;

    public static NavigationView navigationView;
    private static View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_home);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameHome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Home.mAppContext = getApplicationContext();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        header.findViewById(R.id.userImage).setOnClickListener(this);
        header.findViewById(R.id.user_name).setOnClickListener(this);
        header.findViewById(R.id.userEmail).setOnClickListener(this);
    }


    private void login() {
        TextView userName = (TextView) header.findViewById(R.id.user_name);
        if (userName.getText().toString().equalsIgnoreCase("user name")) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        } else {
            Snackbar.make((FrameLayout) findViewById(R.id.frameHome), "You Alreaddy logged.Try Logout First",
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if
                (id == R.id.post_by_subject) {
            Intent intent = new Intent(this, Branch.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.sign_out_drawer) {
            Intent intent = new Intent(this, Login.class);
            intent.putExtra("action", "logout");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onClick(View v) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        switch (v.getId()) {
            case R.id.user_name:
                login();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.userEmail:
                login();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.userImage:
                login();
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
 @Override
    protected void onPostResume() {
        super.onPostResume();
    }

}