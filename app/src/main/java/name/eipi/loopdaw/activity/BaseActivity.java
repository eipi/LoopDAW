package name.eipi.loopdaw.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;

import name.eipi.loopdaw.AudioSession;
import name.eipi.loopdaw.R;
import name.eipi.loopdaw.adapter.CardContentAdapter;
import name.eipi.loopdaw.fragment.CardContentFragment;
import name.eipi.loopdaw.fragment.FavsCardContentFragment;
import name.eipi.loopdaw.fragment.ProjectFragment;
import name.eipi.loopdaw.fragment.CustomWaveformFragment;
import name.eipi.loopdaw.fragment.TrackFragment;
import name.eipi.loopdaw.main.LoopDAWApp;
import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.util.LoopDAWLogger;

/**
 * Created by Damien on 19/02/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public LoopDAWApp app;
//    protected ProjectFragment projectFragment;
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

    public void openInfoDialog(Context current) {
        Dialog dialog = new Dialog(current);
        dialog.setTitle("About LoopDAW");
        dialog.setContentView(R.layout.info);
        TextView title = (TextView) dialog.findViewById(R.id.titleInfoView);
        TextView text = (TextView) dialog.findViewById(R.id.textInfoView);
        TextView link = (TextView) dialog.findViewById(R.id.linkInfoView);

        title.setText("LoopDAW 1.0a, built on 5/17");
        text.setText("To get started, create a project.  Add a track and record something.\n" +
                "Trim the recording to a suitable loop, and then build on top of it!\n\n" +
                "Experimental mode is " + (AudioSession.isExperimentalMode() ? " enabled, careful now!" : "disabled."));
        link.setText("http://www.eipi.name");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    protected void toastMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    protected void createProject(final String projectName) {

        if ((projectName.length() > 0)) {
            Project c = new Project(projectName);
            String baseFileDir = getExternalCacheDir().getAbsolutePath()
                    + File.separator + "projects" + File.separator + projectName + File.separator;
            c.setBaseFilePath(baseFileDir);
            File file = new File(baseFileDir + "project.info");
            file.getParentFile().mkdirs();
            c.save();
            app.projectList.add(c);
            CardContentFragment.dataList.add(c);
            Bundle activityInfo = new Bundle(); // Creates a new Bundle object
            activityInfo.putInt("projectID", app.projectList.indexOf(c));
//            if (CardContentFragment.adapter != null) {
//                try {
//                    CardContentFragment.adapter.notifyDataSetChanged();
//                } catch (Throwable t) {
//                    LoopDAWLogger.getInstance().msg(t.getMessage());
//                }
//            }
            FavsCardContentFragment.refreshFavs();
            CardContentFragment.adapter.notifyDataSetChanged();
            goToActivity(this, EditActivity.class, activityInfo);
        } else {
            Toast.makeText(
                    this,
                    "You must enter a name for the project.",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
