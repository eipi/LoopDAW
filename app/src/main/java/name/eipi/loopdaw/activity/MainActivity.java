package name.eipi.loopdaw.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import name.eipi.loopdaw.NavigationUtils;
import name.eipi.loopdaw.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void actionNew(View view) {
        NavigationUtils.goToActivity(this, NewActivity.class, null);
    }

    public void actionOpen(View view) {
        NavigationUtils.goToActivity(this, OpenActivity.class, null);
    }

    public void actionManage(View view) {
        NavigationUtils.goToActivity(this, ManageActivity.class, null);
    }

    public void actionSettings(View view) {
        NavigationUtils.goToActivity(this, SettingsActivity.class, null);
    }

    public void actionBrowse(View view) {
        NavigationUtils.goToActivity(this, BrowseActivity.class, null);
    }

}
