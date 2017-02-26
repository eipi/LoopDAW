package name.eipi.loopdaw.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
}
