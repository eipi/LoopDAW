package name.eipi.loopdaw.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.model.Track;

/**
 * Created by avd1 on 07/02/2017.
 */

public class TrackListAdapter extends ArrayAdapter<Track> {

    private Context context;
    private View.OnClickListener deleteListener;
    public List<Track> trackList;

    public TrackListAdapter(Context context, View.OnClickListener deleteListener, List<Track> trackList)
    {
        super(context, R.layout.track_line_item, trackList);

        this.context = context;
        this.deleteListener = deleteListener;
        this.trackList = trackList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TrackItem item = new TrackItem(context, parent, deleteListener,
                trackList.get(position));
        return item.view;
    }

    @Override
    public int getCount()
    {
        return trackList.size();
    }

    @Override
    public Track getItem(int position)
    {
        return trackList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getPosition(Track c)
    {
        return trackList.indexOf(c);
    }

}
