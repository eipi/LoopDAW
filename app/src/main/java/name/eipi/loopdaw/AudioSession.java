package name.eipi.loopdaw;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import name.eipi.loopdaw.fragment.CustomWaveformFragment;
import name.eipi.loopdaw.model.Track;
import name.eipi.loopdaw.util.LoopDAWLogger;

/**
 * Created by Damien on 25/02/2017.
 */
public class AudioSession {

    private final static LoopDAWLogger logger = LoopDAWLogger.getInstance();

    private AudioSession() {
        // no public impl.
    }

    public static AudioSession getInstance() {
        return new AudioSession();
    }

    private MediaRecorder mRecorder = null;
    private Map<Track, MediaPlayer> mediaPlayerMap = new HashMap<>();

    public void record(boolean start, Track track) {
        if (start) {
            startRecording(track);
        } else {
            stopRecording();
        }
    }

    public void play(boolean start, Track track) {
        if (start) {
            logger.msg("Starting to play track " + track.getFileName() + " from " +  track.getStartTime() + " to " +  track.getEndTime());
            startPlaying(track);
        } else {
            stopPlaying(track);
        }
    }

    private void startPlaying(final Track track) {

        final MediaPlayer mPlayer = new MediaPlayer();
        mediaPlayerMap.put(track, mPlayer);
        try {
            mPlayer.setDataSource(track.getFileName());
            mPlayer.prepare();
            mPlayer.seekTo(track.getStartTime());
            mPlayer.start();
        }catch(Exception e) {
            logger.msg(this.getClass().getSimpleName() + "prepare() for play failed : " + track.getFileName());
        }
        if (track.getEndTime() != 0) {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        stopPlaying(track);
                        //mPlayer.stop();
                    } catch(Exception e) {
                        logger.msg(this.getClass().getSimpleName() + "prepare() for stop failed : " + track.getFileName());
                    }
                }
            }, track.getEndTime() - track.getStartTime());
        }

    }

    private void stopPlaying(Track track) {
        MediaPlayer mPlayer = mediaPlayerMap.get(track);
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }

    }

    private void startRecording(Track track) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(track.getFileName());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            logger.msg(this.getClass().getSimpleName() + "prepare() for record failed : " + track.getFileName());
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

        for (MediaPlayer mPlayer : mediaPlayerMap.values()) {
            if (mPlayer != null) {
                mPlayer.release();
                mPlayer = null;
            }
        }

    }

    public void playAll(ArrayList<Track> clips) {
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(9);
        SoundPool sp = builder.build();
        Map<Track, Integer> trackIndex = new HashMap<>();
        Map<Track, FileInputStream> inputStreamIndex = new HashMap<>();
        try {
            for (Track track : clips) {
                //todo - prevalidate;
                inputStreamIndex.put(track, new FileInputStream(new File(track.getFileName())));
                int trackId = sp.load(inputStreamIndex.get(track).getFD(), track.getStartTime(), track.getEndTime(), 1);
                trackIndex.put(track, trackId);
            }
        } catch (Exception ex) {
            logger.msg(ex.getMessage());
        }
        for (Integer trackId : trackIndex.values()) {
            sp.play(trackId, 1, 1, 1, -1, 1);
        }

    }
}
