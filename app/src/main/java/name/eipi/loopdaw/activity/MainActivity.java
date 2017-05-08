package name.eipi.loopdaw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import name.eipi.loopdaw.AudioSession;
import name.eipi.loopdaw.R;
import name.eipi.loopdaw.fragment.CardContentFragment;
import name.eipi.loopdaw.fragment.FavsCardContentFragment;
import name.eipi.loopdaw.fragment.ListContentFragment;
import name.eipi.loopdaw.main.LoopDAWApp;
import name.eipi.loopdaw.util.NavigationUtils;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // Adding Toolbar to Main screen
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

//            TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
//            tabs.addTab(tabs.newTab().setText("Tab 1"));
//            tabs.addTab(tabs.newTab().setText("Tab 2"));
//            tabs.addTab(tabs.newTab().setText("Tab 3"));

            // Setting ViewPager for each Tabs
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);
//        // Set Tabs inside Toolbar
            TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);

            // Create Navigation drawer and inflate layout
            final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

            // Adding menu icon to Toolbar
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
                supportActionBar.setDisplayHomeAsUpEnabled(true);
            }

            // Set behavior of Navigation drawer
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        // This method will trigger on item Click of navigation menu
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            // Set item in checked state
                            // menuItem.setChecked(true);

                            int id = menuItem.getItemId();

        if (id == R.id.menu_home_btn) {
//            NavigationUtils.goToActivity(menuItem.getActionView().getContext(), MainActivity.class, null);
        } else if (id == R.id.menu_info_btn) {
            openInfoDialog(navigationView.getContext());

        } else if (id == R.id.menu_signin_btn) {
            NavigationUtils.goToActivity(navigationView.getContext(), SignInActivity.class, null);

        } else if (id == R.id.menu_logs) {
            NavigationUtils.goToActivity(navigationView.getContext(), LogViewActivity.class, null);

        } else if (id == R.id.menu_toggle_crazy_mode) {
            AudioSession.toggleExperimentalMode();
            if (menuItem.isCheckable()) {
                menuItem.setChecked(!menuItem.isChecked());
            }
        }

//                            //noinspection SimplifiableIfStatement
//                            if (id == android.R.id.home) {
//                                mDrawerLayout.openDrawer(GravityCompat.START);
//                            } else if (id == R.id.menu_home_btn) {
//                                NavigationUtils.goToActivity(this, MainActivity.class, null);
//                            } else if (id == R.id.menu_info_btn) {
//                                openInfoDialog(this);
//                            } else if (id == R.id.menu_signin_btn) {
//                                goToActivity(this, SignInActivity.class, null);
//                            } else if (id == R.id.menu_signin__sc_btn) {
//                                goToActivity(this, SignInActivity.class, null);
//                            } else if (id == R.id.menu_logs) {
//                                goToActivity(this, LogViewActivity.class, null);
//                            }

                            // TODO: handle navigation
                            // Closing drawer on item click
                            mDrawerLayout.closeDrawers();
                            return true;
                        }
                    });

            // Adding Floating Action Button to bottom right of main view
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("New Project");
                    // I'm using fragment here so I'm using getView() to provide ViewGroup
                    // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                    View viewInflated = LayoutInflater.from(v.getContext()).inflate(R.layout.input_text_popup, null, false);
                    // Set up the input
                    final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    builder.setView(viewInflated);

                    // Set up the buttons
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            createProject(input.getText().toString());
                            Snackbar.make(v, "Created new project \"" + input.getText().toString()
                                            + "\"!", Snackbar.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        switch (id) {
//            case R.id.home : mDrawerLayout.openDrawer(GravityCompat.START); break;
//            case R.id.menu_home_btn : NavigationUtils.goToActivity(this, MainActivity.class, null); break;
//            case R.id.menu_info_btn : openInfoDialog(this); break;
//            case R.id.sign_in_button : NavigationUtils.goToActivity(this, SignInActivity.class, null); break;
//
//        }
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else if (id == R.id.menu_home_btn) {
//            NavigationUtils.goToActivity(this, MainActivity.class, null);
        } else if (id == R.id.menu_info_btn) {
            openInfoDialog(this);
        } else if (id == R.id.menu_signin_btn) {
            goToActivity(this, SignInActivity.class, null);
//        } else if (id == R.id.menu_signin__sc_btn) {
//            goToActivity(this, SignInActivity.class, null);
        } else if (id == R.id.menu_logs) {
            goToActivity(this, LogViewActivity.class, null);
        } else if (id == R.id.menu_toggle_crazy_mode) {
            AudioSession.toggleExperimentalMode();
            if (item.isCheckable()) {
                item.setChecked(!item.isChecked());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        LoopDAWApp thisApp = (LoopDAWApp) getApplication();
        Adapter adapter = new Adapter(getSupportFragmentManager());
//        adapter.addFragment(new ListContentFragment(), "List");
        CardContentFragment allProjects = new CardContentFragment();
        FavsCardContentFragment favourites = new FavsCardContentFragment();

        adapter.addFragment(allProjects, "All Projects");
        adapter.addFragment(favourites, "Favourites");

        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void editProject(View v) {

    }

}
