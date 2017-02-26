package name.eipi.loopdaw.fragment;

import android.os.Parcelable;

import com.semantive.waveformandroid.waveform.WaveformFragment;

/**
 * Created by Damien on 25/02/2017.
 */

public class CustomWaveformFragment extends WaveformFragment {

    private String filePath;

    private Integer startTime;
    private Integer endTime;


    /**
     * Provide path to your audio file.
     *
     * @return
     */
    @Override
    public String getFileName() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;

    }

    public int getStartTime() {
//        return this.mStartText.getText().toString();
        if (startTime != null) {
            return startTime;
        } else {
            if (this.mWaveformView != null && this.mWaveformView.isInitialized()) {
                return Double.valueOf(this.mWaveformView.pixelsToSeconds(this.mStartPos) * 1000).intValue();
            }
            return 0;
        }
    }

    public int getEndTime() {
        if (endTime != null) {
            return endTime;
        } else {
            if (this.mWaveformView != null && this.mWaveformView.isInitialized()) {
                return Double.valueOf(this.mWaveformView.pixelsToSeconds(this.mEndPos) * 1000).intValue();
            }
            return -1;
        }

    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
