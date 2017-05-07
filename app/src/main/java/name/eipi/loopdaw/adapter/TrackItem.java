package name.eipi.loopdaw.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.activity.RecordActivity;
import name.eipi.loopdaw.activity.ViewerActivity;
import name.eipi.loopdaw.main.LoopDAWApp;
import name.eipi.loopdaw.model.Track;

/**
 * Created by Damien on 26/02/2017.
 */

public class TrackItem {

    View view;

    public TrackItem(Context context, ViewGroup parent,
                     View.OnClickListener deleteListener, final Track track)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.track_line_item, parent, false);
        view.setId(track.getId());

        updateControls(track);

        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDeleteTrack);
        imgDelete.setTag(track);
        imgDelete.setOnClickListener(deleteListener);

        ImageView imgMute = (ImageView) view.findViewById(R.id.imgMuteTrack);
        imgMute.setTag(track);
        imgMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                track.setMute(!track.isMute());
                updateControls(track);
            }
        });

    }

    private void updateControls(Track track) {
        ((TextView) view.findViewById(R.id.rowTrackName)).setText("Track" + track.getId());

        long duration = track.getEndTime() - track.getStartTime();
        ((TextView) view.findViewById(R.id.rowSubTitle)).setText("Duration: " + (duration / 1000l) + "." + (duration%1000) + "s");

        ImageView mute = (ImageView) view.findViewById(R.id.imgMuteTrack);
        mute.setImageResource(track.isMute() ? R.drawable.ic_mute : R.drawable.ic_unmute);

    }

}
