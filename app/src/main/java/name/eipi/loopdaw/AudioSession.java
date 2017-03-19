package name.eipi.loopdaw;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.model.Track;
import name.eipi.loopdaw.util.LoopDAWLogger;

/**
 * Created by Damien on 25/02/2017.
 */
public class AudioSession {

    private final static boolean useSoundPool = Boolean.TRUE;

    private final static LoopDAWLogger logger = LoopDAWLogger.getInstance();

    private final Context context;

    private final SimpleExoPlayer player;

    private AudioSession(Context ctx) {
        this.context = ctx;
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory trackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(trackSelectionFactory);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);

        SimpleExoPlayerView view = (SimpleExoPlayerView) ((Activity) ctx).findViewById(R.id.player_view);
        // Bind the player to the view.
        view.setPlayer(player);
    }

    public static AudioSession getInstance(Context context) {
        return new AudioSession(context);
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

    public void play(boolean start, Project project) {
        if (start) {
            logger.msg("Starting to play tracks for project " + project.getName());
            if (useSoundPool) {
                playAll(project.getClips());
            } else {
                for (Track t : project.getClips()) {
                    startPlaying(t);
                }
            }
        } else {
            if (useSoundPool) {
                stopAll();
            } else {
                for (Track t : project.getClips()) {
                    stopPlaying(t);
                }
            }
        }
    }

    @Deprecated
    private void startPlaying(final Track track) {
        logger.msg("Starting to play track " + track.getFileName() + " from " + track.getStartTime() + " to " + track.getEndTime());
        final MediaPlayer mPlayer = new MediaPlayer();
        mediaPlayerMap.put(track, mPlayer);
        try {
            mPlayer.setDataSource(track.getFileName());
            mPlayer.prepare();
            mPlayer.seekTo(track.getStartTime());
            mPlayer.start();
        } catch (Exception e) {
            logger.msg(this.getClass().getSimpleName() + "prepare() for play failed : " + track.getFileName());
        }
        if (track.getEndTime() != 0) {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        stopPlaying(track);
                        startPlaying(track);
                        //mPlayer.stop();
                    } catch (Exception e) {
                        logger.msg(this.getClass().getSimpleName() + "prepare() for stop failed : " + track.getFileName());
                    }
                }
            }, track.getEndTime() - track.getStartTime());
        }

    }

    @Deprecated
    private void stopPlaying(Track track) {
        MediaPlayer mPlayer = mediaPlayerMap.get(track);
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }
        mediaPlayerMap.remove(track);
    }

    private void startRecording(Track track) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(track.getFileName());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);

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

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "LoopDAW"));
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        for (Track track : clips) {
            MediaSource audioSource = new ExtractorMediaSource(Uri.parse(new File(track.getFileName()).toURI().toString()),
                    dataSourceFactory, extractorsFactory, null, null);
            LoopingMediaSource loopSource = new LoopingMediaSource(audioSource);
            // Prepare the player with the source.
            player.prepare(loopSource);
        }

        player.setPlayWhenReady(true);

    }

    public void stopAll() {
        if (player != null) {
            player.stop();
        }
    }
}
