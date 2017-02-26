package name.eipi.loopdaw.fragment;

import com.semantive.waveformandroid.waveform.WaveformFragment;
import com.semantive.waveformandroid.waveform.view.MarkerView;

import name.eipi.loopdaw.model.Track;

/**
 * Created by Damien on 25/02/2017.
 */

public class CustomWaveformFragment extends WaveformFragment {

    Track track;

    public void link(Track track) {
        this.track = track;
    }

    /**
     * Provide path to your audio file.
     *
     * @return
     */
    @Override
    public String getFileName() {
        return track.getFileName();
    }

    @Override
    public void markerTouchMove(MarkerView marker, float x) {
        super.markerTouchMove(marker, x);
        if (this.mWaveformView != null && this.mWaveformView.isInitialized()) {
            track.setStartTime(Double.valueOf(this.mWaveformView.pixelsToSeconds(this.mStartPos) * 1000).intValue());
            track.setEndTime(Double.valueOf(this.mWaveformView.pixelsToSeconds(this.mEndPos) * 1000).intValue());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mWaveformView != null && this.mWaveformView.isInitialized()) {
            this.mStartPos = this.mWaveformView.secondsToPixels(track.getStartTime() / 1000.0);
            this.mEndPos = this.mWaveformView.secondsToPixels(track.getEndTime() / 1000.0);
        }

    }

}
