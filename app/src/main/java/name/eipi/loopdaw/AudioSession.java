package name.eipi.loopdaw;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ClippingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.common.api.BooleanResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.model.Track;
import name.eipi.loopdaw.util.LoopDAWLogger;

/**
 * Created by Damien on 25/02/2017.
 */
public class AudioSession {

    private final static Collection<SimpleExoPlayer> players = new ArrayList<>();

    private final static LoopDAWLogger logger = LoopDAWLogger.getInstance();

    private static boolean experimentalMode = false;

    private final Context context;

    private final TrackSelector trackSelector;
    private final LoadControl loadControl;

    // Produces DataSource instances through which media data is loaded.
    private final DataSource.Factory dataSourceFactory;
    // Produces Extractor instances for parsing the media data.
    private final ExtractorsFactory extractorsFactory;

    public static void toggleExperimentalMode() {
        experimentalMode = !experimentalMode;
    }

    public static boolean isExperimentalMode() {
        return experimentalMode;
    }

    private AudioSession(Context ctx) {
        this.context = ctx;
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory trackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector =
                new DefaultTrackSelector(trackSelectionFactory);

        // 2. Create a default LoadControl
        loadControl = new DefaultLoadControl();

        dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "LoopDAW"));
        extractorsFactory = new DefaultExtractorsFactory();
//
//        PlaybackControlView view = (PlaybackControlView) ((Activity) ctx).findViewById(R.id.player_view);
//
//        // Bind the player to the view.
//        if (view != null) {
//            view.setPlayer(player);
//        }
        mediaPlayerMap = new ConcurrentHashMap<>();
    }

    public static AudioSession getInstance(Context context) {
        AudioSession audioSession = new AudioSession(context);
        return audioSession;
    }

    private MediaRecorder mRecorder = null;
    private ConcurrentMap<Track, SimpleExoPlayer> mediaPlayerMap;


    public void record(boolean start, Track track, Project project) {
        if (start) {
            startRecording(track, project);
        } else {
            stopRecording();
        }
    }

    public void play(boolean start, Project project) {
        if (start) {
            logger.msg("Starting to play tracks for project " + project.getName());
//                playAll(project.getClips());
            for (Track t : project.getClips()) {
                startPlaying(t);
            }
        } else {
//                stopAll();
            for (SimpleExoPlayer s : players) {
                stopPlaying(s);
            }
            mediaPlayerMap.clear();
//            for (Track t : project.getClips()) {
//                stopPlaying(t);
//            }
            players.clear();

        }
    }

    @Deprecated
    private void startPlaying(final Track track) {
        if (!track.isMute()) {
            logger.msg("Starting to play track " + track.getFilePath() + " from " + track.getStartTime() + " to " + track.getEndTime());
            try {
                MediaSource audioSource = new ExtractorMediaSource(Uri.parse(new File(track.getFilePath()).toURI().toString()),
                        dataSourceFactory, extractorsFactory, null, null);
                Long normailzedStartTime = Long.valueOf(track.getStartTime()) * 1000l;
                Long normailzedEndTime = Long.valueOf(track.getEndTime()) * 1000l;
                if (normailzedEndTime < 1) {
                    normailzedEndTime = Long.MIN_VALUE;
                }

                ClippingMediaSource clip = new ClippingMediaSource(audioSource, normailzedStartTime, normailzedEndTime);
                LoopingMediaSource loopSource = new LoopingMediaSource(clip);
                SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
                player.prepare(loopSource);
                player.setPlayWhenReady(Boolean.TRUE);
//            player.seekTo(track.getStartTime());
//
//            mPlayer.setDataSource(track.getFilePath());
//            mPlayer.prepare();
//            mPlayer.seekTo(track.getStartTime());
//            mPlayer.start();

            } catch (Exception e) {
                logger.msg(this.getClass().getSimpleName() + "prepare() for play failed : " + track.getFilePath());
            }
            if (experimentalMode) {
                if (track.getEndTime() != 0) {
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            try {
                                stopPlaying(track);
                                startPlaying(track);
                                //mPlayer.stop();
                            } catch (Exception e) {
                                logger.msg(this.getClass().getSimpleName() + "prepare() for stop failed : " + track.getFilePath());
                            }
                        }
                    }, track.getEndTime() - track.getStartTime());
                }
            }
        }
    }

    private void stopPlaying(Track track) {
        SimpleExoPlayer mPlayer = mediaPlayerMap.get(track);
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
//        mediaPlayerMap.remove(track);
    }

    private void stopPlaying(SimpleExoPlayer mPlayer) {
        if (mPlayer != null) {
//            if (mPlayer.isPlaying()) {
            mPlayer.stop();
//            }
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void startRecording(Track track, Project project) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(track.getFilePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);

        try {
            mRecorder.prepare();
            //play(true, project);
            mRecorder.start();
        } catch (IllegalStateException | IOException e) {
            logger.msg(this.getClass().getSimpleName() + " prepare() for record failed : " + track.getFilePath());
        }

    }

    private void stopRecording() {
        try {
            mRecorder.stop();
//            for (SimpleExoPlayer s : mediaPlayerMap.values()) {
//                stopPlaying(s);
//            }
            mRecorder.release();
            mRecorder = null;
        } catch (IllegalStateException ex) {
            logger.msg(this.getClass().getSimpleName() + " stop audio failed : " + mRecorder);
        }
    }


    public void cleanUp() {
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        for (SimpleExoPlayer mPlayer : mediaPlayerMap.values()) {
            if (mPlayer != null) {
                mPlayer.release();
                mPlayer = null;
            }
        }

    }

//    public void playAll(ArrayList<Track> clips) {
//        // This is the MediaSource representing the media to be played.
//        for (Track track : clips) {
//            MediaSource audioSource = new ExtractorMediaSource(Uri.parse(new File(track.getFilePath()).toURI().toString()),
//                    dataSourceFactory, extractorsFactory, null, null);
//            LoopingMediaSource loopSource = new LoopingMediaSource(audioSource);
//
//            // Prepare the player with the source.
//            loops.add(loopSource);
//            player.prepare(loopSource);
//        }
//        LoopingMediaSource[] loopingMediaSources = new LoopingMediaSource[loops.size()];
//        loops.toArray(loopingMediaSources);
//        MergingMediaSource ms = new MergingMediaSource(loopingMediaSources);
//////        player.prepare(ms);
////        // / Prepare the player with the source.
////        player.prepare(ms);
//        player.setPlayWhenReady(true);
//    }
//
//    public void stopAll() {
//        if (player != null) {
//            player.stop();
//        }
//    }
}
