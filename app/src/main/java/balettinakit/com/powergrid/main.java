package balettinakit.com.powergrid;

import android.content.Intent;
import android.content.res.Configuration;
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

import java.io.IOException;

public class main extends AppCompatActivity {
    private Class fragmentClass;
    private Fragment fragment;
    private Toolbar toolbar;

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawer_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        NavigationView nvDrawer = findViewById(R.id.nvView);
        mDrawer.addDrawerListener(drawerToggle);
        setupDrawerContent(nvDrawer);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        Class fragmentClass = fragment_main.class;


        try {
            fragment = (Fragment) fragmentClass.newInstance();


            dataFetcher.doFetch(() -> {
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
                    //ToDo add error handling
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            //ToDo add error handling
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    /**
     * sets up the drawer menu selected event
     * @param navigationView the navigation view
     */

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    /**
     * Method that handles drawer menu selections
     *
     * @param menuItem is the selected menuItem
     */

    public void selectDrawerItem(final MenuItem menuItem) {
        Boolean doTransition = true;
        switch (menuItem.getItemId()) {

            case R.id.nav_main:
                doTransition = false;

                dataFetcher.doFetch(() -> {

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
                        menuItem.setChecked(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                        //ToDo add error handling
                    }
                });


                break;
            case R.id.nav_devices:
                fragmentClass = fragment_devices.class;
                break;

            //Todo add settings
                /* case R.id.nav_settings:
                    fragmentClass = fragment_settings.class;
                    break; */

            case R.id.nav_share:

                //Share the app
                fragmentClass = fragment_main.class;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_to_share));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.send_to)));
                doTransition = false;
                break;

            default:
                //Go to main screen
                fragmentClass = fragment_main.class;
        }

        //Spaghetti implementation of switching fragment when needed not needed eg. when sharing the app
        //ToDo add better way of handling transitions

        if (doTransition) {

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                //ToDo add error hadling
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
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);

    }


}
