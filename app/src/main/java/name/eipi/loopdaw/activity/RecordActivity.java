package name.eipi.loopdaw.activity;

import android.Manifest;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.io.File;

import name.eipi.loopdaw.AudioSession;
import name.eipi.loopdaw.R;
import name.eipi.loopdaw.fragment.CustomWaveformFragment;
import name.eipi.loopdaw.fragment.TrackFragment;
import name.eipi.loopdaw.main.LoopDAWApp;
import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.model.Track;

public class RecordActivity extends BaseActivity {

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private AudioSession audioSession;

    boolean mStartRecording = true;
    boolean mStartPlaying = true;

    private Project project;
    private Track track;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        //if (!permissionToRecordAccepted ) finish();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        audioSession = AudioSession.getInstance();
        Bundle extras = getIntent().getExtras();
        int projectContext = extras.getInt("projectID", -1);
        int trackContext = extras.getInt("trackID", -1);
        if (projectContext != -1) {
            project = ((LoopDAWApp) getApplication()).projectList.get(projectContext);
            track = (Track) project.getClips().get(trackContext);
        }

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

    }

    @Override
    public void onStop() {
        super.onStop();
        audioSession.cleanUp();
    }

    public void actionRecord(View v) {

        audioSession.record(mStartRecording, track);
        Button button = (Button) v.findViewById(R.id.record_button);
        if (mStartRecording) {
            button.setText("Stop recording");
        } else {
            button.setText("Start recording");

        }
        mStartRecording = !mStartRecording;

    }

    public void actionPlay(View v) {

        Button button = (Button) v.findViewById(R.id.play_button);
        audioSession.play(mStartPlaying, track);
        if (mStartPlaying) {
            button.setText("Stop playing");
        } else {
            button.setText("Start playing");
        }
        mStartPlaying = !mStartPlaying;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (track != null) {
            // Store values between instances here
            SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor ed = mPrefs.edit();
            ed.putInt("projectID", app.projectList.indexOf(project)); // value to store
            ed.putInt("trackID", track.getId()); // value to store
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        String filePath = mPrefs.getString("filePath", null);
        if (filePath != null) {
            int projectId = mPrefs.getInt("projectID", -1);
            int trackId = mPrefs.getInt("trackID", -1);
            project = ((LoopDAWApp) getApplication()).projectList.get(projectId);
            track = project.getClips().get(trackId);
        }

    }

}
