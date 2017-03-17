package name.eipi.loopdaw.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.fragment.ProjectFragment;

public class OpenActivity extends BaseActivity {

    TextView recentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        recentList = (TextView) findViewById(R.id.recentlyAddedListEmpty);

        projectFragment = ProjectFragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.project_fragment_layout, projectFragment);
        transaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(app.projectList.isEmpty()) {
            recentList.setText(getString(R.string.recentlyViewedListEmptyMessage));
        } else {
            recentList.setText(null);
        }

    }

}