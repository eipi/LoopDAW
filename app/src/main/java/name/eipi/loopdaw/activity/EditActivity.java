package name.eipi.loopdaw.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import name.eipi.loopdaw.AudioSession;
import name.eipi.loopdaw.R;
import name.eipi.loopdaw.fragment.ProjectFragment;
import name.eipi.loopdaw.fragment.TrackFragment;
import name.eipi.loopdaw.main.LoopDAWApp;
import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.model.Track;
import name.eipi.loopdaw.util.AudioMixer;
import name.eipi.loopdaw.util.LoopDAWLogger;

public class EditActivity extends BaseActivity {

    private static final LoopDAWLogger logger =  LoopDAWLogger.getInstance();

    TextView trackList;
    private Project project;
    boolean mStartPlaying = true;
    private AudioSession audioSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Bundle extras = getIntent().getExtras();
        int projectContext = extras.getInt("projectID", -1);
        if (projectContext != -1) {
            project = ((LoopDAWApp) getApplication()).projectList.get(projectContext);
        }

        trackList = (TextView) findViewById(R.id.trackListEmpty);

        trackFragment = TrackFragment.newInstance(project);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.track_fragment_layout, trackFragment);
        transaction.commit();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(project.getClips().isEmpty()) {
            trackList.setText(getString(R.string.trackListEmptyMessage));
        } else {
            trackList.setText(null);
        }

    }

    public void actionNewTrack(View view) {
        Track track = Track.newInstance(project);
        project.getClips().add(track);
        TrackFragment.listAdapter.notifyDataSetChanged();

    }

    public void actionPlay(View view) {

        ImageButton button = (ImageButton) view.findViewById(R.id.play_button);
        if (mStartPlaying) {
            logger.msg("EditActivity.actionPlayAll - in mStartPlaying");
            button.setImageResource(R.drawable.ic_stop_black_24dp);
        } else {
            logger.msg("EditActivity.actionPlayAll - in !mStartPlaying");
            button.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
        if (audioSession == null) {
            audioSession = AudioSession.getInstance(this);
        }
        audioSession.play(mStartPlaying, project);
        mStartPlaying = !mStartPlaying;
    }

    public void actionEdit(View view) {

        //TODO - allow edit of project info?


    }

    public void actionExport(View view) {
        new AudioMixer(project).renderProject();
        //TODO - allow export of project data to Google Drive.


    }


}
