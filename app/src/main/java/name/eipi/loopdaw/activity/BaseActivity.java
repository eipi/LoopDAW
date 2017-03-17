package name.eipi.loopdaw.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.fragment.ProjectFragment;
import name.eipi.loopdaw.fragment.CustomWaveformFragment;
import name.eipi.loopdaw.fragment.TrackFragment;
import name.eipi.loopdaw.main.LoopDAWApp;
import name.eipi.loopdaw.model.Project;

/**
 * Created by Damien on 19/02/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public LoopDAWApp app;
    protected Bundle activityInfo;
    protected ProjectFragment projectFragment;
    protected TrackFragment trackFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (LoopDAWApp) getApplication();
    }

    /**
     * Dupe in NavigationUtils.
     */
    @Deprecated
    protected void goToActivity(Activity current,
                                Class<? extends Activity> activityClass,
                                Bundle bundle) {
        Intent newActivity = new Intent(current, activityClass);

        if (bundle != null) newActivity.putExtras(bundle);

        current.startActivity(newActivity);
    }

    public void openInfoDialog(Activity current) {
        Dialog dialog = new Dialog(current);
        dialog.setTitle("About LoopDAW");
        dialog.setContentView(R.layout.info);

        TextView currentVersion = (TextView) dialog
                .findViewById(R.id.versionTextView);
        currentVersion.setText("1.0.0");

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void menuInfo(MenuItem m)
    {
        openInfoDialog(this);
    }

    public void menuHelp(MenuItem m)
    {
        toastMessage("Not yet implemented!");
    }

    public void menuHome(MenuItem m)
    {
        goToActivity(this, MainActivity.class, null);
    }

    protected void toastMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void delete(View view) {
        if (view.getTag() instanceof Project) {
            Project project = (Project) view.getTag();
            app.projectList.remove(project);
            ProjectFragment.listAdapter.remove(project);
            ProjectFragment.listAdapter.notifyDataSetChanged();
        }

    }

}
