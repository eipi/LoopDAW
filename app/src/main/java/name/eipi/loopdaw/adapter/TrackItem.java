package name.eipi.loopdaw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.model.Track;

/**
 * Created by Damien on 26/02/2017.
 */

public class TrackItem {

    View view;

    public TrackItem(Context context, ViewGroup parent,
                       View.OnClickListener deleteListener, Track track)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.track_line_item, parent, false);
        view.setId(track.getId());

//        updateControls(track);

        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        imgDelete.setTag(track);
        imgDelete.setOnClickListener(deleteListener);
    }

    private void updateControls(Track track) {
        ((TextView) view.findViewById(R.id.rowTrackName)).setText("Track" + track.getId());
        ImageView imgIcon = (ImageView) view.findViewById(R.id.RowImage);

        if (track.getFileName() != null) {
            imgIcon.setImageResource(R.drawable.ic_favourite_on);
        } else {
            imgIcon.setImageResource(R.drawable.ic_favourite_off);
        }

    }

}
