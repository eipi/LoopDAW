package name.eipi.loopdaw.fragment;

import android.text.Editable;
import android.text.TextWatcher;

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
        return track.getFilePath();
    }


    @Override
    protected synchronized void updateDisplay() {
        super.updateDisplay();
        updateValues();
    }

//    @Override
//    public void markerTouchMove(MarkerView marker, float x) {
//        super.markerTouchMove(marker, x);
////        updateValues();
//        if (this.mWaveformView != null && this.mWaveformView.isInitialized()) {
//            track.setStartTime(Double.valueOf(this.mWaveformView.pixelsToSeconds(this.mStartPos) * 1000).intValue());
//            track.setEndTime(Double.valueOf(this.mWaveformView.pixelsToSeconds(this.mEndPos) * 1000).intValue());
//        }
//    }

    private void updateValues() {
        if (this.mWaveformView != null && this.mWaveformView.isInitialized()) {
            track.setStartTime(Double.valueOf(this.mWaveformView.pixelsToSeconds(this.mStartPos) * 1000).intValue());
            track.setEndTime(Double.valueOf(this.mWaveformView.pixelsToSeconds(this.mEndPos) * 1000).intValue());
        }
//        track.getProject().save();

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (this.mWaveformView != null && this.mWaveformView.isInitialized()) {
//            this.mStartPos = this.mWaveformView.secondsToPixels(track.getStartTime() / 1000.0);
//            this.mEndPos = this.mWaveformView.secondsToPixels(track.getEndTime() / 1000.0);
//        }
//    }

    @Override
    protected void finishOpeningSoundFile() {
        //super.finishOpeningSoundFile();
        this.mWaveformView.setSoundFile(this.mSoundFile);
        this.mWaveformView.recomputeHeights(this.mDensity);
        this.mMaxPos = this.mWaveformView.maxPos();
        this.mLastDisplayedStartPos = -1;
        this.mLastDisplayedEndPos = -1;
        this.mTouchDragging = false;
        this.mOffset = 0;
        this.mOffsetGoal = 0;
        this.mFlingVelocity = 0;

        // Replaced below call with custom interception as below.
        //this.resetPositions();
        // BEGIN CUSTOM INTERCEPTION
        if (this.mWaveformView != null && this.mWaveformView.isInitialized()) {
            if (track.getStartTime() != 0) {
                this.mStartPos = this.mWaveformView.secondsToPixels(track.getStartTime() / 1000.0);
            } else {
                this.mStartPos = 0;
            }
            if (track.getEndTime() != 0) {
                this.mEndPos = this.mWaveformView.secondsToPixels(track.getEndTime() / 1000.0);
            } else {
                this.mEndPos = this.mMaxPos;
            }
        }
        // END CUSTOM INTERCEPTION
        this.mCaption = this.mSoundFile.getFiletype() + ", " + this.mSoundFile.getSampleRate() + " Hz, " + this.mSoundFile.getAvgBitrateKbps() + " kbps, " + this.formatTime(this.mMaxPos) + " " + this.getResources().getString(com.semantive.waveformandroid.R.string.time_seconds);
        this.mInfo.setText(this.mCaption);
        this.mProgressDialog.dismiss();
        this.updateDisplay();
    }

    public TextWatcher mTextWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if(CustomWaveformFragment.this.mStartText.hasFocus()) {
                try {
                    CustomWaveformFragment.this.mStartPos = CustomWaveformFragment.this.mWaveformView.secondsToPixels(Double.parseDouble(CustomWaveformFragment.this.mStartText.getText().toString()));
                    CustomWaveformFragment.this.updateDisplay();
                    updateValues();
                } catch (NumberFormatException var4) {
                    ;
                }
            }

            if(CustomWaveformFragment.this.mEndText.hasFocus()) {
                try {
                    CustomWaveformFragment.this.mEndPos = CustomWaveformFragment.this.mWaveformView.secondsToPixels(Double.parseDouble(CustomWaveformFragment.this.mEndText.getText().toString()));
                    CustomWaveformFragment.this.updateDisplay();
                    updateValues();
                } catch (NumberFormatException var3) {
                    ;
                }
            }

        }
    };

}
