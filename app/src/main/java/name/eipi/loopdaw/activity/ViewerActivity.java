package name.eipi.loopdaw.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.fragment.TrackFragment;
import name.eipi.loopdaw.main.LoopDAWApp;
import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.model.Track;

public class ViewerActivity extends AppCompatActivity {

    private Project project;
    private Track track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        Bundle extras = getIntent().getExtras();
        int projectContext = extras.getInt("projectID", -1);
        int trackContext = extras.getInt("trackID", -1);
        project = ((LoopDAWApp) getApplication()).projectList.get(projectContext);
        track = (Track) project.getClips().get(trackContext);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.viewer_frame, track.getWaveform());
        transaction.commit();

    }


    public void backToProject(View view) {
        Bundle activityInfo = new Bundle(); // Creates a new Bundle object
        activityInfo.putInt("projectID", ((LoopDAWApp) getApplication()).projectList.indexOf(project));
        Intent goView = new Intent(this, EditActivity.class);
        goView.putExtras(activityInfo);
        this.startActivity(goView); // Launch the Intent
    }

}
