package name.eipi.loopdaw.activity;

import android.Manifest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

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
    private Timer t;
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
        audioSession = AudioSession.getInstance(this);
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

        audioSession.record(mStartRecording, track, project);
        ImageButton button = (ImageButton) v.findViewById(R.id.record_button);
        ViewGroup row = (ViewGroup) v.getParent();
        TextView textView = (TextView) row.findViewById(R.id.record_timer);
        if (mStartRecording) {
            startTimer(textView);
            button.setImageResource(R.drawable.ic_stop_black_24dp);
        } else {
            stopTimer();
            Bundle activityInfo = new Bundle(); // Creates a new Bundle object
            activityInfo.putInt("projectID", ((LoopDAWApp) getApplication()).projectList.indexOf(project));
            activityInfo.putInt("trackID", track.getId());
            Intent goView = new Intent(this, ViewerActivity.class);
            goView.putExtras(activityInfo);
            this.startActivity(goView); // Launch the Intent
        }
        mStartRecording = !mStartRecording;
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

    private void stopTimer() {
        if (t != null) {
            t.cancel();
        }
    }

    private void startTimer(final TextView textView) {
        t = new Timer("hello", true);
        t.schedule(new TimerTask() {
            int tenths = 0, minute =0, seconds = 0, hour = 0;
            @Override
            public void run() {
                textView.post(new Runnable() {

                    public void run() {
                        tenths ++;
                        if (tenths == 10) {
                            tenths = 0;
                            seconds++;
                        }
                        if (seconds == 60) {
                            seconds = 0;
                            minute++;
                        }
                        if (minute == 60) {
                            minute = 0;
                            hour++;
                        }
                        textView.setText(""
                                + (hour > 9 ? hour : ("0" + hour)) + " : "
                                + (minute > 9 ? minute : ("0" + minute))
                                + " : "
                                + (seconds > 9 ? seconds : "0" + seconds)
                                + ("." + tenths));

                    }
                });

            }
        }, 0, 100);
    }

}
