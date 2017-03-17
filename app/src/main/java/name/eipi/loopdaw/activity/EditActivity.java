package name.eipi.loopdaw.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import name.eipi.loopdaw.AudioSession;
import name.eipi.loopdaw.R;
import name.eipi.loopdaw.fragment.ProjectFragment;
import name.eipi.loopdaw.fragment.TrackFragment;
import name.eipi.loopdaw.main.LoopDAWApp;
import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.model.Track;
import name.eipi.loopdaw.util.LoopDAWLogger;

public class EditActivity extends BaseActivity {

    private static final LoopDAWLogger logger =  LoopDAWLogger.getInstance();

    TextView trackList;
    private Project project;
    boolean mStartPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Bundle extras = getIntent().getExtras();
        int projectContext = extras.getInt("projectID", -1);
        int trackContext = extras.getInt("trackID", -1);
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

    public void actionPlayWithMediaPlayer(View view) {

        Button button = (Button) view.findViewById(R.id.play_m_button);
        if (mStartPlaying) {
            logger.msg("EditActivity.actionPlayAll - in mStartPlaying");
            button.setText("Stop MP");
        } else {
            logger.msg("EditActivity.actionPlayAll - in !mStartPlaying");
            button.setText("Play MP");
        }

        AudioSession audioSession = AudioSession.getInstance();
        //audioSession.playAll(project.getClips()); //todo-makeasync
        for (Track t : project.getClips()) {
            audioSession.play(mStartPlaying, t);
        }
        mStartPlaying = !mStartPlaying;
    }

    public void actionPlayWithSoundPool(View view) {

        Button button = (Button) view.findViewById(R.id.play_button);
        if (mStartPlaying) {
            logger.msg("EditActivity.actionPlayAll - in mStartPlaying");
            button.setText("Stop SP");
        } else {
            logger.msg("EditActivity.actionPlayAll - in !mStartPlaying");
            button.setText("Play SP");
        }

        AudioSession audioSession = AudioSession.getInstance();
        audioSession.playAll(project.getClips()); //todo-makeasync
        mStartPlaying = !mStartPlaying;
    }

    //actionPlayWithSoundPool

}
