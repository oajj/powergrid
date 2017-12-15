package balettinakit.com.powergrid;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.FirebaseApp;

import java.io.IOException;

public class main extends AppCompatActivity {
    Class fragmentClass;
    Fragment fragment;
        private DrawerLayout mDrawer;
        private Toolbar toolbar;
        private NavigationView nvDrawer;
    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
        // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
        private ActionBarDrawerToggle drawerToggle;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.drawer_main);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerToggle = setupDrawerToggle();
            nvDrawer = findViewById(R.id.nvView);
            mDrawer.addDrawerListener(drawerToggle);
            setupDrawerContent(nvDrawer);

            final FragmentManager fragmentManager = getSupportFragmentManager();
            Class fragmentClass = fragment_main.class;

            fragment = null;
            try {
                fragment = (Fragment) fragmentClass.newInstance();


                new mainFragment().execute(new fetchData() {
                    @Override
                    public void doInBackground() {
                        try {
                            Connection c = new Connection(getResources().getString(R.string.host), 1234);
                            c.login(0, "");
                            Bundle args = new Bundle();
                            int[] i = c.houseGetHistory(0, -1);
                            args.putIntArray("data", i);
                            fragment.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // The action bar home/up action should open or close the drawer.
            switch (item.getItemId()) {
                case android.R.id.home:
                    mDrawer.openDrawer(GravityCompat.START);
                    return true;
            } if (drawerToggle.onOptionsItemSelected(item)) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }


        private void setupDrawerContent(NavigationView navigationView) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            selectDrawerItem(menuItem);
                            return true;
                        }
                    });
        }

        public void selectDrawerItem(MenuItem menuItem) {
            final MenuItem item = menuItem;
            fragment = null;
            Boolean doTransition = true;
            switch(menuItem.getItemId()) {

                case R.id.nav_main:
                    doTransition = false;
                    new mainFragment().execute(new fetchData() {
                        @Override
                        public void doInBackground() {
                            try {
                                fragmentClass = fragment_main.class;
                                Connection c = new Connection(getResources().getString(R.string.host), 1234);
                                c.login(0, "");
                                Bundle args = new Bundle();
                                int[] i = c.houseGetHistory(0, -1);
                                args.putIntArray("data", i);
                                fragment = (Fragment) fragmentClass.newInstance();
                                fragment.setArguments(args);
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                                item.setChecked(true);
                                // setTitle(item.getTitle());

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case R.id.nav_devices:
                    fragmentClass = fragment_devices.class;
                    break;

                /* case R.id.nav_settings:
                    fragmentClass = fragment_settings.class;
                    break; */

                case R.id.nav_share:
                        fragmentClass = fragment_main.class;
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! \nI found this cool service that reduces my electricity usage and energy bills! \nCheck it out here [link]");
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, getString(R.string.send_to)));
                        doTransition = false;
                        break;
                default:
                    fragmentClass = fragment_main.class;
            }
            if(doTransition) {
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                menuItem.setChecked(true);
                setTitle(menuItem.getTitle());
            }
            mDrawer.closeDrawers();

        }

    @NonNull
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
            drawerToggle.syncState();
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            // Pass any configuration change to the drawer toggles
            drawerToggle.onConfigurationChanged(newConfig);

        }

    private class mainFragment extends AsyncTask<fetchData, Void, String> {

        @Override
        protected String doInBackground(fetchData... params) {
            params[0].doInBackground();

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    }
