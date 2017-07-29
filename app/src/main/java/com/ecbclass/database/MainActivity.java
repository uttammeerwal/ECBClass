package com.ecbclass.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecbclass.NetworkCheker;
import com.ecbclass.database.fragment.MyPostsFragment;
import com.ecbclass.database.fragment.MyTopPostsFragment;
import com.ecbclass.database.fragment.RecentPostsFragment;
import com.ecbclass.user_activity.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.quickstart.database.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private static View header;
    private static ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameHome);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View inflate = layoutInflater.inflate(R.layout.activity_main, null, true);
        frameLayout.addView(inflate);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);

        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setMessage("Server not reachable.Check internet Connection");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new RecentPostsFragment(),
                    new MyPostsFragment(),
                    new MyTopPostsFragment(),
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.heading_recent),
                    getString(R.string.heading_my_posts),
                    getString(R.string.heading_my_top_posts)
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
               return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }

        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Button launches NewPostActivity
        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewPostActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            Intent intent = new Intent(this, Branch.class);
            intent.putExtra("action", "logout");
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            this.finish();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationUI();
    }

    private void updateNavigationUI() {
        isOnlne();
        FirebaseUser currentUser =
                FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("ECBClass", Context.MODE_PRIVATE);
            String userDetail = sharedPreferences.getString("userDetail", "");
            String phoneDetails = sharedPreferences.getString("phone", "");

            if (phoneDetails != null && !phoneDetails.equalsIgnoreCase("")) {
                TextView userName = (TextView) header.findViewById(R.id.user_name);
                ImageView userImage = (ImageView) header.findViewById(R.id.userImage);
                String name = phoneDetails.toString();
                userName.setText(name);
                userImage.setImageResource(R.drawable.blue_call_icon);
                TextView userEmail = (TextView) header.findViewById(R.id.userEmail);
                userEmail.setText("Logged by Phone Number");
                navigationView.getMenu().setGroupVisible(R.id.logout_grp, true);


            } else if (userDetail != "") {
                try {
                    JSONObject user = new JSONObject(userDetail);
                    TextView userName = (TextView) header.findViewById(R.id.user_name);
                    TextView userEmail = (TextView) header.findViewById(R.id.userEmail);
                    ImageView userImage = (ImageView) header.findViewById(R.id.userImage);
                    String email = user.get("email").toString();
                    String name = user.get("name").toString();
                    String image = user.get("image").toString();
                    userEmail.setText(email);
                    userName.setText(name);
                    Picasso.with(getApplicationContext()).load(image)
                            .placeholder(R.drawable.google)
                            .error(R.drawable.ic_action_account_circle_40)
                            .into(userImage);
                    navigationView.getMenu().setGroupVisible(R.id.logout_grp, true);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ImageView userImage = (ImageView) header.findViewById(R.id.userImage);
                userImage.setImageResource(R.drawable.ic_account_circle_white_24dp);
                TextView userName = (TextView) header.findViewById(R.id.user_name);
                TextView userEmail = (TextView) header.findViewById(R.id.userEmail);
                userEmail.setText("someone@example.com");
                userName.setText("User Name");
                navigationView.getMenu().setGroupVisible(R.id.logout_grp, false);
            }

        } else {

            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateNavigationUI();
    }


    public void isOnlne() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            changeStatus(true);
        } else {
            changeStatus(false);
        }
    }

    // Method to change the text status
    public void changeStatus(boolean isConnected) {
        // Change status according to boolean value
        if (isConnected) {
            progress.dismiss();
        } else {
            progress.show();
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        NetworkCheker.activityPaused();// On Pause notify the Application
    }

    @Override
    protected void onResume() {

        super.onResume();
        NetworkCheker.activityResumed();// On Resume notify the Application
    }
}
