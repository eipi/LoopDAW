package name.eipi.loopdaw.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.activity.EditActivity;
import name.eipi.loopdaw.activity.ViewerActivity;
import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.model.Track;
import name.eipi.loopdaw.util.NavigationUtils;

/**
 * Created by eipi on 30/04/2017.
 */

public class ListContentAdapter  extends RecyclerView.Adapter<ListContentAdapter.ListViewHolder> {

    private final List<Track> content;

    public ListContentAdapter(Context context, final List<Track> contentIn) {
        Resources resources = context.getResources();
        // todo - replace with java-based resource injection.
        content = contentIn;
    }

    @Override
    public ListContentAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListContentAdapter.ListViewHolder(LayoutInflater.from(parent.getContext()), parent);

    }

    @Override
    public void onBindViewHolder(ListContentAdapter.ListViewHolder holder, int position) {
        Track track = content.get(position);
        holder.title.setText(track.getName());
        holder.description.setText(track.getFilePath());
        holder.currentItem = track;
    }

    @Override
    public int getItemCount() {
        return content.size();
    }



    public static class ListViewHolder extends RecyclerView.ViewHolder {

        public Track currentItem;
        public TextView title;
        public TextView subtitle;
        public TextView description;

        public ListViewHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.item_card, parent, false));
            title = (TextView) itemView.findViewById(R.id.card_title);
            subtitle = (TextView) itemView.findViewById(R.id.card_subtitle);
            description = (TextView) itemView.findViewById(R.id.card_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    Bundle activityInfo = new Bundle(); // Creates a new Bundle object
                    activityInfo.putInt("projectID", getAdapterPosition());
                    activityInfo.putInt("trackID", currentItem.getId());

                    NavigationUtils.goToActivity(v.getContext(), ViewerActivity.class, activityInfo);
                }
            });
        }
    }

}
