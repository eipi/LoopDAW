package name.eipi.loopdaw.activity;

import android.Manifest;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.io.File;

import name.eipi.loopdaw.AudioSession;
import name.eipi.loopdaw.R;
import name.eipi.loopdaw.fragment.CustomWaveformFragment;

public class RecordActivity extends AppCompatActivity {

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private AudioSession audioSession;
    private CustomWaveformFragment customWaveformFragment;

    private String baseFileDir;


    boolean mStartRecording = true;
    boolean mStartPlaying = true;


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
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.record_container, new CustomWaveformFragment())
//                    .commit();
//        }
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

//        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.activity_record, null);
//        FrameLayout ll = (FrameLayout) view.findViewById(R.id.record_container);
        baseFileDir = getExternalCacheDir().getAbsolutePath()
                + File.separator + "audio" + File.separator;
        File projectInfo = new File(baseFileDir + "project.info");
        if (!projectInfo.exists()) {
            projectInfo.getParentFile().mkdirs();
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        String filePath = savedInstanceState.getString("filePath");
        if (filePath != null) {
            int startTime = savedInstanceState.getInt("startTime");
            int endTime = savedInstanceState.getInt("endTime");
            customWaveformFragment = new CustomWaveformFragment();
            customWaveformFragment.setFilePath(filePath);
            customWaveformFragment.setStartTime(startTime);
            customWaveformFragment.setEndTime(endTime);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        audioSession.cleanUp();
    }

    public void actionRecord(View v) {

        String recordFilePath = baseFileDir + "Test.3gp";

        customWaveformFragment = new CustomWaveformFragment();
        customWaveformFragment.setFilePath(recordFilePath);

        audioSession.record(mStartRecording, recordFilePath);
        Button button = (Button) v.findViewById(R.id.record_button);
        if (mStartRecording) {
            button.setText("Stop recording");
        } else {
            button.setText("Start recording");
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.record_inner_frame, customWaveformFragment);
            transaction.commit();
        }
        mStartRecording = !mStartRecording;

    }

    public void actionPlay(View v) {


        Button button = (Button) v.findViewById(R.id.play_button);
        audioSession.play(mStartPlaying, baseFileDir + "Test.3gp", customWaveformFragment);
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
        if (customWaveformFragment != null) {
            // Store values between instances here
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();  // Put the values from the UI
            editor.putString("filePath", customWaveformFragment.getFileName()); // value to store
            editor.putInt("startTime", customWaveformFragment.getStartTime()); // value to store
            editor.putInt("endTime", customWaveformFragment.getEndTime()); // value to store
            // Commit to storage
            editor.commit();
        }
    }
}
