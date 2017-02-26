package name.eipi.loopdaw;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.text.DateFormat;

import name.eipi.loopdaw.fragment.CustomWaveformFragment;

/**
 * Created by Damien on 25/02/2017.
 */
public class AudioSession {

    private AudioSession() {
        // no public impl.
    }

    public static AudioSession getInstance() {
        return new AudioSession();
    }

    private MediaRecorder mRecorder = null;
    private MediaPlayer   mPlayer = null;

    public void record(boolean start, String fName) {
        if (start) {
            startRecording(fName);
        } else {
            stopRecording();
        }
    }

    public void play(boolean start, String fName, CustomWaveformFragment customWaveformFragment) {
        if (start) {
            startPlaying(fName, customWaveformFragment);
        } else {
            stopPlaying();
        }
    }

    private void startPlaying(String fName, CustomWaveformFragment customWaveformFragment) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fName);
            mPlayer.prepare();
            mPlayer.start();
        }catch(Exception e) {
            Log.e(this.getClass().getSimpleName(), "prepare() failed");
        }

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                try {
                    mPlayer.stop();
                }catch(Exception e) {
                    Log.e(this.getClass().getSimpleName(), "prepare() failed");
                }
            }
        }, customWaveformFragment.getEndTime() - customWaveformFragment.getStartTime());
    }

    private void stopPlaying() {
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording(String fName) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(fName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }


    public void cleanUp() {
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

}
